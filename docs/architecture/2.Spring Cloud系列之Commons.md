# 什么是`Spring Cloud Commons`

Spring Cloud框架包括如下功能：

- 分布式多版本配置管理
- 服务注册与发现
- 路由
- 微服务调用
- 负载均衡
- 断路器
- 分布式消息

Spring Cloud Commons包含实现这一切要加载的基础组件的接口，以及Spring Cloud启动如何加载，加载哪些东西。其中：

- spring cloud context：包括Spring Cloud应用需要加载的`ApplicationContext`的内容

- spring cloud common: 包括如下几个基本组件以及其加载配置：

- - 服务注册接口：`org.springframework.cloud.serviceregistry`包
  - 服务发现接口：`org.springframework.cloud.discovery`包
  - 负载均衡接口：`org.springframework.cloud.loadbalancer`包
  - 断路器接口： `org.springframework.cloud.circuitbreaker`包

- spring cloud loadbalancer：类似于ribbon，并且是ribbon的替代品。实现了上述负载均衡接口的组件

这个系列我们要讲述的是 spring cloud common 这个模块，spring cloud loadbalancer 还有 spring cloud context 将会在另一个单独的系列。

# Spring 与 Spring Boot 背景知识补充

我们在看一个 Spring Cloud 模块源代码时，需要记住任何一个 Spring Cloud 模块都是基于 Spring Boot 扩展而来的，这个扩展一般是通过 spring.factories SPI 机制。任何一个 Spring Cloud 模块源代码都可以以这个为切入点进行理解

## spring.factories SPI 机制

那么什么是 SPI（Service Provider）呢？在系统设计中，为了模块间的协作，往往会设计统一的接口供模块之间的调用。面向的对象的设计里，我们一般推荐模块之间基于接口编程，模块之间不对实现类进行硬编码，而是将指定哪个实现置于程序之外指定。Java 中默认的 SPI 机制就是通过 `ServiceLoader` 来实现，简单来说就是通过在`META-INF/services`目录下新建一个名称为接口全限定名的文件，内容为接口实现类的全限定名，之后程序通过代码:

```java
//指定加载的接口类，以及用来加载类的类加载器，如果类加载器为 null 则用根类加载器加载
ServiceLoader<SpiService> serviceLoader = ServiceLoader.load(SpiService.class, someClassLoader);
Iterator<SpiService> iterator = serviceLoader.iterator();
while (iterator.hasNext()){
    SpiService spiService = iterator.next();
}
```

获取指定的实现类。

在 Spring 框架中，这个类是`SpringFactoriesLoader`，需要在`META-INF/spring.factories`文件中指定接口以及对应的实现类，例如 Spring Cloud Commons 中的：

```java
# Environment Post Processors
org.springframework.boot.env.EnvironmentPostProcessor=\
org.springframework.cloud.client.HostInfoEnvironmentPostProcessor
```

其中指定了`EnvironmentPostProcessor`的实现`HostInfoEnvironmentPostProcessor`。

同时，Spring Boot 中会通过`SpringFactoriesLoader.loadXXX`类似的方法读取所有的`EnvironmentPostProcessor`的实现类并生成 Bean 到 ApplicationContext 中：

EnvironmentPostProcessorApplicationListener

```java
//这个类也是通过spring.factories中指定ApplicationListener的实现而实现加载的，这里省略
public class EnvironmentPostProcessorApplicationListener implements SmartApplicationListener, Ordered {
    //创建这个Bean的时候，会调用
    public EnvironmentPostProcessorApplicationListener() {
		this(EnvironmentPostProcessorsFactory
				.fromSpringFactories(EnvironmentPostProcessorApplicationListener.class.getClassLoader()));
	}
}
```

EnvironmentPostProcessorsFactory

```java
static EnvironmentPostProcessorsFactory fromSpringFactories(ClassLoader classLoader) {
	return new ReflectionEnvironmentPostProcessorsFactory(
	        //通过 SpringFactoriesLoader.loadFactoryNames 获取文件中指定的实现类并初始化
			SpringFactoriesLoader.loadFactoryNames(EnvironmentPostProcessor.class, classLoader));
}
```

## spring.factories 的特殊使用 - EnableAutoConfiguration

`META-INF/spring.factories` 文件中不一定指定的是接口以及对应的实现类，例如：

```java
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.cloud.loadbalancer.config.LoadBalancerAutoConfiguration,\
org.springframework.cloud.loadbalancer.config.BlockingLoadBalancerClientAutoConfiguration,\
```

其中`EnableAutoConfiguration`是一个注解，`LoadBalancerAutoConfiguration`与`BlockingLoadBalancerClientAutoConfiguration`都是配置类并不是`EnableAutoConfiguration`的实现。那么这个是什么意思呢？`EnableAutoConfiguration`是一个注解，`LoadBalancerAutoConfiguration`与`BlockingLoadBalancerClientAutoConfiguration`都是配置类。`spring.factories`这里是另一种特殊使用，记录要载入的 Bean 类。`EnableAutoConfiguration`在注解被使用的时候，这些 Bean 会被加载。这就是`spring.factories`的另外一种用法。

`EnableAutoConfiguration`是 Spring-boot 自动装载的核心注解。有了这个注解，Spring-boot 就可以自动加载各种`@Configuration`注解的类。那么这个机制是如何实现的呢？

来看下`EnableAutoConfiguration`的源码 `EnableAutoConfiguration`

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
	String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";
	//排除的类
	Class<?>[] exclude() default {};
	//排除的Bean名称
	String[] excludeName() default {};
}
```

我们看到了有 `@Import` 这个注解。这个注解是 Spring 框架的一个很常用的注解，是 Spring 基于 Java 注解配置的主要组成部分。

## `@Import`注解的作用

`@Import`注解提供了`@Bean`注解的功能，同时还有原来`Spring`基于 xml 配置文件里的`<import>`标签组织多个分散的xml文件的功能，当然在这里是组织多个分散的`@Configuration`的类。这个注解的功能与用法包括

### 1. 引入其他的`@Configuration`

假设有如下接口和两个实现类：

```java
package com.test
interface ServiceInterface {
    void test();
}

class ServiceA implements ServiceInterface {

    @Override
    public void test() {
        System.out.println("ServiceA");
    }
}

class ServiceB implements ServiceInterface {

    @Override
    public void test() {
        System.out.println("ServiceB");
    }
}
```

两个`@Configuration`，其中`ConfigA``@Import``ConfigB`:

```java
package com.test
@Import(ConfigB.class)
@Configuration
class ConfigA {
    @Bean
    @ConditionalOnMissingBean
    public ServiceInterface getServiceA() {
        return new ServiceA();
    }
}

@Configuration
class ConfigB {
    @Bean
    @ConditionalOnMissingBean
    public ServiceInterface getServiceB() {
        return new ServiceB();
    }
}
```

通过`ConfigA`创建`AnnotationConfigApplicationContext`，获取`ServiceInterface`，看是哪种实现：

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(ConfigA.class);
    ServiceInterface bean = ctx.getBean(ServiceInterface.class);
    bean.test();
}
```

输出为：`ServiceB`.证明`@Import`的优先于本身的的类定义加载。

### 2. 直接初始化其他类的`Bean`

在Spring 4.2之后，`@Import`可以直接指定实体类，加载这个类定义到`context`中。例如把上面代码中的`ConfigA`的`@Import`修改为`@Import(ServiceB.class)`，就会生成`ServiceB`的`Bean`到容器上下文中，之后运行`main`方法，输出为：`ServiceB`.证明`@Import`的优先于本身的的类定义加载.

### 3. 指定实现`ImportSelector`(以及`DefferredServiceImportSelector`)的类，用于个性化加载

指定实现`ImportSelector`的类，通过`AnnotationMetadata`里面的属性，动态加载类。`AnnotationMetadata`是`Import`注解所在的类属性（如果所在类是注解类，则延伸至应用这个注解类的非注解类为止）。

需要实现`selectImports`方法，返回要加载的`@Configuation`或者具体`Bean`类的全限定名的`String`数组。

```java
package com.test;
class ServiceImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //可以是@Configuration注解修饰的类，也可以是具体的Bean类的全限定名称
        return new String[]{"com.test.ConfigB"};
    }
}

@Import(ServiceImportSelector.class)
@Configuration
class ConfigA {
    @Bean
    @ConditionalOnMissingBean
    public ServiceInterface getServiceA() {
        return new ServiceA();
    }
}
```

再次运行`main`方法，输出：`ServiceB`.证明`@Import`的优先于本身的的类定义加载。一般的，框架中如果基于`AnnotationMetadata`的参数实现动态加载类，一般会写一个额外的`Enable`注解，配合使用。例如：

```java
public class ServiceAImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //这里的importingClassMetadata针对的是使用@EnableService的非注解类
        //因为`AnnotationMetadata`是`Import`注解所在的类属性，如果所在类是注解类，则延伸至应用这个注解类的非注解类为止
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnableService.class.getName(),true);
        String name = (String) map.get("name");
        if (Objects.equals(name,"B")){
            return new String[]{"com.syraven.cloud.config.ConfigB"};
        }
        return new String[0];
    }
}
```

之后，在`ConfigA`中增加注解`@EnableService(name = "B")`

```java
@Configuration
@EnableService(name = "B")
public class ConfigA {

    @Bean
    @ConditionalOnMissingBean
    public ServiceInterface getServiceA(){
        return new ServiceA();
    }
}
```

再次运行`main`方法，输出：`ServiceB`.

还可以实现`DeferredImportSelector`接口,这样`selectImports`返回的类就都是最后加载的，而不是像`@Import`注解那样，先加载。例如：

```java
public class DefferredServiceImportSelector implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnableService.class.getName(), true);
        String name = (String) map.get("name");
        if (Objects.equals(name, "B")) {
            return new String[]{"com.syraven.cloud.config.ConfigB"};
        }
        return new String[0];
    }
}
```

修改`EnableService`注解：

```java
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
@Import(DefferredServiceImportSelector.class)
@interface EnableService {
    String name();
}
```

这样`ConfigA`就优先于`DefferredServiceImportSelector`返回的`ConfigB`加载，执行`main`方法，输出：`ServiceA`

### 4. 指定实现`ImportBeanDefinitionRegistrar`的类，用于个性化加载

与`ImportSelector`用法与用途类似，但是如果我们想重定义`Bean`，例如动态注入属性，改变`Bean`的类型和`Scope`等等，就需要通过指定实现`ImportBeanDefinitionRegistrar`的类实现。例如：

定义`ServiceC`

```java
public class ServiceC implements ServiceInterface {

    private final String name;

    public ServiceC(String name) {
        this.name = name;
    }

    @Override
    public void test() {
        System.out.println(name);
    }
}
```

定义`ServiceImportBeanDefinitionRegistrar`动态注册`ServiceC`，修改`EnableService`

```java
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
//@Import(ServiceImportSelector.class)
//@Import(DefferredServiceImportSelector.class)
@Import(ServiceImportBeanDefinitionRegistrar.class)
public @interface EnableService {

    String name();
}


public class ServiceImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnableService.class.getName(), true);
        String name = (String) map.get("name");
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ServiceC.class)
                //增加构造参数
                .addConstructorArgValue(name);
        //注册Bean
        registry.registerBeanDefinition("serviceC", beanDefinitionBuilder.getBeanDefinition());
    }

}

```

ImportBeanDefinitionRegistrar` 在 `@Bean` 注解之后加载，所以要修改`ConfigA`去掉其中被`@ConditionalOnMissingBean`注解的`Bean`，否则一定会生成`ConfigA`的`ServiceInterface

```java
//@Import(ServiceImportSelector.class)
@Configuration
//@EnableService(name = "B")
@EnableService(name = "TestServiceC")
public class ConfigA {

    /*@Bean
    @ConditionalOnMissingBean
    public ServiceInterface getServiceA(){
        return new ServiceA();
    }*/
}
```

之后运行`main`，输出：`TestServiceC`

## Spring Boot 核心自动装载的实现原理

上面我们提到了`@EnableAutoConfiguration`注解里面的：

```
@Import(AutoConfigurationImportSelector.class)
```

属于`@Import`注解的第三种用法，也就是通过具体的`ImportSelector`进行装载，实现其中的`selectImports`接口返回需要自动装载的类的全限定名称。这里的`AutoConfigurationImportSelector`实现是： `AutoConfigurationImportSelector`

```java
@Override
public String[] selectImports(AnnotationMetadata annotationMetadata) {
    //`spring.boot.enableautoconfiguration`这个属性没有指定为false那就是启用了Spring Boot自动装载，否则就是没启用。没启用的话，返回空数组
	if (!isEnabled(annotationMetadata)) {
		return NO_IMPORTS;
	}
	//获取要加载的类,详情见下面源代码
	AutoConfigurationEntry autoConfigurationEntry = getAutoConfigurationEntry(annotationMetadata);
	return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
}

//获取要加载的类
protected AutoConfigurationEntry getAutoConfigurationEntry(AnnotationMetadata annotationMetadata) {
	//`spring.boot.enableautoconfiguration`这个属性没有指定为false那就是启用了Spring Boot自动装载，否则就是没启用。没启用的话，返回空数组
	if (!isEnabled(annotationMetadata)) {
		return EMPTY_ENTRY;
	}
	//获取注解
	AnnotationAttributes attributes = getAttributes(annotationMetadata);
	//从spring.factories读取所有key为org.springframework.boot.autoconfigure.EnableAutoConfiguration的类
	List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
	//去重
	configurations = removeDuplicates(configurations);
	//根据EnableAutoConfiguration注解的属性去掉要排除的类
	Set<String> exclusions = getExclusions(annotationMetadata, attributes);
	checkExcludedClasses(configurations, exclusions);
	configurations.removeAll(exclusions);
	configurations = getConfigurationClassFilter().filter(configurations);
	//发布AutoConfigurationImportEvent事件
	fireAutoConfigurationImportEvents(configurations, exclusions);
	return new AutoConfigurationEntry(configurations, exclusions);
}
```

## Spring Boot中的 ApplicationContext 的层级是什么

ApplicationContext 是 spring 用来容纳管理 beans 以及其生命周期的容器。ApplicationContext 的分层规定了bean的界限以及可以复用的 bean。关于 ApplicationContext 层级可以参考官方文档，这里我们通过一个简单的例子来说明下 ApplicationContext 层级以及其中的bean界限，例如某些 bean 可以被多个 ApplicationContext 共享，同时某些 bean 只在某个 ApplicationContext 生效，不同 ApplicationContext 可以声明同名或者同类型的bean这样。我们将实现一个下图所示的 ApplicationContext 结构：

![图片](https://upload-images.jianshu.io/upload_images/18022990-3a2e1f84d3445851.png?imageMogr2/auto-orient/strip|imageView2/2/format/webp)

我们会实现，一个 parent context 与三个对应 child context 的结构。

首先定义Parent context：

Bean类：

```java
package com.syraven.cloud.spring.context.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RootBean {
    private String name;
}
```

Context类：

```java
package com.syraven.cloud.spring.context.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:/root.yaml",factory = YamlPropertyLoaderFactory.class)
public class RootContext {

    @Bean
    public RootBean getFatherBean(){
        RootBean rootBean = new RootBean();
        rootBean.setName("root");
        return rootBean;
    }
}
```

root.yml：

```
# 配置这些主要是将actuator相关接口暴露出来。
management:
  endpoint:
    health:
      show-details: always
  endpoints:
    jmx:
      exposure:
        exclude: '*'
    web:
      exposure:
        include: '*'
```

由于我们使用了yml，这里需要我们自定义一个`YamlPropertyLoaderFactory`用于加载yml配置：

```java
package com.syraven.cloud.spring.context.bean;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;

public class YamlPropertyLoaderFactory extends DefaultPropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        if (resource == null){
            return super.createPropertySource(name, resource);
        }

        return new YamlPropertySourceLoader().load(resource.getResource().getFilename(), resource.getResource()).get(0);
    }
}
```

定义child context的公共Bean类：

```java
package com.syraven.cloud.spring.context.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChildBean {

    private RootBean fatherBean;
    private String name;
}
```

定义ChildContext1：

```java
package com.syraven.cloud.spring.context.config.child1;

import com.syraven.cloud.spring.context.bean.ChildBean;
import com.syraven.cloud.spring.context.bean.RootBean;
import com.syraven.cloud.spring.context.bean.YamlPropertyLoaderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication(scanBasePackages= {"com.syraven.cloud.controller"})
@PropertySource(value = "classpath:/bean-config-1.yaml", factory = YamlPropertyLoaderFactory.class)
public class ChildContext1 {

    @Bean
    public ChildBean getChildBean(@Value("$spring.application.name") String name, RootBean fatherBean){
        ChildBean childBean = new ChildBean();
        childBean.setFatherBean(fatherBean);
        childBean.setName(name);
        return childBean;
    }
}
```

bean-config-1.yaml：

```yaml
server:
  port: 8080
spring:
  application:
    name: child1
```

接下来分别是ChildContext2，ChildContext3的：

```java
package com.syraven.cloud.spring.context.config.child2;

import com.syraven.cloud.spring.context.bean.ChildBean;
import com.syraven.cloud.spring.context.bean.RootBean;
import com.syraven.cloud.spring.context.bean.YamlPropertyLoaderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication(scanBasePackages= {"com.syraven.cloud.controller"})
@PropertySource(value = "classpath:/bean-config-2.yaml", factory = YamlPropertyLoaderFactory.class)
public class ChildContext2 {

    @Bean
    public ChildBean getChildBean(@Value("${spring.application.name}") String name,
                                  RootBean fatherBean) {
        ChildBean childBean = new ChildBean();
        childBean.setFatherBean(fatherBean);
        childBean.setName(name);
        return childBean;
    }
}
```

```yaml
server:
  port: 8081
spring:
  application:
    name: child2

management:
  endpoint:
    health:
      show-details: always
  endpoints:
    jmx:
      exposure:
        exclude: '*'
    web:
      exposure:
        include: '*'
```

```java
package com.syraven.cloud.spring.context.config.child3;

import com.syraven.cloud.spring.context.bean.ChildBean;
import com.syraven.cloud.spring.context.bean.RootBean;
import com.syraven.cloud.spring.context.bean.YamlPropertyLoaderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
java
@SpringBootApplication(scanBasePackages= {"com.syraven.cloud.controller"})
@PropertySource(value = "classpath:/bean-config-3.yaml", factory = YamlPropertyLoaderFactory.class)
public class ChildContext3 {

    @Bean
    public ChildBean getChildBean(@Value("$spring.application.name") String name, RootBean fatherBean){
        ChildBean childBean = new ChildBean();
        childBean.setFatherBean(fatherBean);
        childBean.setName(name);
        return childBean;
    }
}
```

```yaml
server:
  port: 8082
spring:
  application:
    name: child3

management:
  endpoint:
    health:
      show-details: always
  endpoints:
    jmx:
      exposure:
        exclude: '*'
    web:
      exposure:
        include: '*'
```

测试接口`TestController`：

```java
package com.syraven.cloud.controller;

import com.syraven.cloud.spring.context.bean.ChildBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @Autowired
    private ChildBean childBean;

    @RequestMapping("/test")
    public ChildBean getChildBean() {
        return childBean;
    }

}
```

启动类：

```java
package com.syraven.cloud.spring.context;

import com.syraven.cloud.spring.context.bean.RootContext;
import com.syraven.cloud.spring.context.config.child1.ChildContext1;
import com.syraven.cloud.spring.context.config.child2.ChildContext2;
import com.syraven.cloud.spring.context.config.child3.ChildContext3;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


public class ContextMain {

    public static void main(String[] args) {
        SpringApplicationBuilder appBuilder =
                new SpringApplicationBuilder()
                        .sources(RootContext.class)
                        //第一个子context用child，剩下的都用sibling
                        .child(ChildContext1.class)
                        .sibling(ChildContext2.class)
                        .sibling(ChildContext3.class);
        ConfigurableApplicationContext applicationContext = appBuilder.run();
    }
}
```

启动后，访问`http://127.0.0.1:8080/test`返回：

```json
{"fatherBean":{"name":"root"},"name":"child1"}
```

访问`http://127.0.0.1:8081/test`返回：

```json
{"fatherBean":{"name":"root"},"name":"child2"}
```

访问`http://127.0.0.1:8082/test`返回：

```json
{"fatherBean":{"name":"root"},"name":"child3"}
```

访问`http://127.0.0.1:8080/actuator/beans`会有类似于下面的返回(省略了不关心的bean)：

```json
{
	"contexts": {
		"application-1": {
			"beans": {
				"getChildBean": {
					"aliases": [],
					"scope": "singleton",
					"type": "com.syraven.cloud.spring.context.bean.ChildBean",
					"resource": "com.syraven.cloud.spring.context.config.child2.ChildContext2",
					"dependencies": [
						"getFatherBean"
					]
				},
				"childContext2": {
					"aliases": [],
					"scope": "singleton",
					"type": "com.syraven.cloud.spring.context.config.child2.ChildContext2$$EnhancerBySpringCGLIB$$26f80b15",
					"resource": null,
					"dependencies": []
				}
				.......
			},
			"parentId": "application"
		},
		"application": {
			"beans": {
				"getFatherBean": {
					"aliases": [],
					"scope": "singleton",
					"type": "com.syraven.cloud.spring.context.bean.RootBean",
					"resource": "com.hopegaming.scaffold.spring.context.config.root.RootContext",
					"dependencies": []
				},
				"rootContext": {
					"aliases": [],
					"scope": "singleton",
					"type": "com.syraven.cloud.spring.context.bean.RootContext$$EnhancerBySpringCGLIB$$18d9c26f",
					"resource": null,
					"dependencies": []
				}
				.......
			},
			"parentId": null
		}
	}
}
```

通过这个例子，想必大家对于 ApplicationContext 层级有了一定的理解

## Bean 加载条件

我们会经常看到`@Conditional`相关的注解，例如`@ConditionalOnBean`还有`@ConditionalOnClass`等等，这些注解提供了自动装载时候根据某些条件加载不同类的灵活性。`@Conditional`注解是 spring-context 提供的特性，Spring Boot 在这个注解的基础上，提供了更多具体的条件配置注解，包括：

- `@ConditionalOnBean`，如果当前 ApplicationContext 的 BeanFactory 已经包含这些 Bean，则满足条件。与之相反的是 `@ConditionalOnMissingBean`，如果当前 ApplicationContext 的 BeanFactory 不包含这些 Bean，则满足条件。
- `@ConditionalOnClass`，如果当前 classpath 中有这些类，则满足条件。与之相反的是`@ConditionalOnMissingClass`，如果当前 classpath 中没有这些类，则满足条件
- `@ConditionalOnProperty`，指定属性是否存在，并且值满足`havingValue`指定的值（没设置就是不为`false`就行），`matchIfMissing`代表如果属性不存在代表条件满足还是不满足。

以上几个注解是比较常用的，剩下的例如`ConditionalOnCloudPlatform`这些不太常用，这里先不提了。

如果有多个类似的`@Conditional`注解作用于同一个方法或者类，这些加载条件是“And”的关系。

## Configuration 加载顺序

由于 Bean 加载条件的复杂性，有时候我们想某些 Configuration 类先加载，某些在特定的 Configuration 加载完之后再加载。例如：

```json
@Configuration
public class FirstConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public Service service1() {
        ......
    }
}
```

```json
@Configuration
public class SecondConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public Service service1() {
        ......
    }
}
```

假设这两个类在不同 jar 包，我们没有办法确定最后创建的是哪一个类的 `Service`，这时候我们就需要用到一些决定 Configuration 加载顺序的注解。注意这里的 Configuration 加载顺序仅仅是 Bean 定义加载顺序，主要是为了限制上面提到的 Bean 加载条件的判断顺序，而不是创建 Bean 的顺序。Bean 创建的顺序主要由 Bean 依赖决定以及`@DependsOn`注解限制。

相关的注解如下：

- `@AutoConfigureAfter` 指定当前 Configuration 在 某个 Configuration 之后加载。
- `@AutoConfigureBefore` 指定当前 Configuration 在 某个 Configuration 之前加载。
- `@AutoConfigureOrder` 类似于`@Order`注解，指定当前 Configuration 的加载序号，默认是 0 ，越小越先加载。

## Bean 排序

对于同一类型的 Bean（实现了同一接口的 Bean），我们可以用一个 List 进行自动装载，例如：

```java
public interface Service {
    void test();
}
@Componenet
public class ServiceA implements Service {
    @Override
    public void test() {
        System.out.println("ServiceA");
    }
}
@Componenet
public class ServiceB implements Service {
    @Override
    public void test() {
        System.out.println("ServiceB");
    }
}

@Componenet
public class Test {
    @Autowired
    private List<Service> services;
}
```

`private List<Service> services` 中就会有 `serviceA` 和 `serviceB` 这两个 Bean，但是谁在前谁在后呢？可以通过`@Order`注解指定。

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@Documented
public @interface Order {

	/**
	 * The order value.
	 * <p>Default is {@link Ordered#LOWEST_PRECEDENCE}.
	 * @see Ordered#getOrder()
	 */
	int value() default Ordered.LOWEST_PRECEDENCE;

}
```

值越小，越靠前。
