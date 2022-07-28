# Spring Cloud LoadBalancer核心源码

通过 `LoadBalancerClientFactory` 知道默认配置类为 `LoadBalancerClientConfiguration`. 并且获取微服务名称可以通过 `environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);`

**LoadBalancerClientFactory**

```java
public static final String NAMESPACE = "loadbalancer";
public static final String PROPERTY_NAME = "loadbalancer.client.name";

public LoadBalancerClientFactory() {
    super(LoadBalancerClientConfiguration.class, "loadbalancer", "loadbalancer.client.name");
}
```

查看配置类 `LoadBalancerClientConfiguration`，我们可以发现这个类主要定义两种 Bean，分别是 `ReactorLoadBalancer<ServiceInstance>` 和 `ServiceInstanceListSupplier`。

`ReactorLoadBalancer` 是负载均衡器，主要提供根据服务名称获取服务实例列表并从从中选择的功能。

**ReactorLoadBalancer**

```java
Mono<Response<T>> choose(Request request);
```

在默认配置中的实现是：

***LoadBalancerClientConfiguration***

```java
@Bean
@ConditionalOnMissingBean
public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(Environment environment, LoadBalancerClientFactory loadBalancerClientFactory) {
  	//获取微服务的名称  
  	String name = environment.getProperty("loadbalancer.client.name");
    //创建 RoundRobinLoadBalancer 
    	//注意这里注入的是 LazyProvider，这主要因为在注册这个 Bean 的时候相关的 Bean 可能还没有被加载注册，利用 LazyProvider 而不是直接注入所需的 Bean 防止报找不到 Bean 注入的错误。
    return new RoundRobinLoadBalancer(loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
}
```

可以看出，默认配置的 `ReactorLoadBalancer` 实现是 `RoundRobinLoadBalancer`。这个负载均衡器实现很简单，有一个原子类型的 `AtomicInteger position`，从 `ServiceInstanceListSupplier` 中读取所有的服务实例列表，然后对于 `position` 原子加1，对列表大小取模，返回列表中这个位置的服务实例 `ServiceInstance`。

**RoundRobinLoadBalancer**

```java
public Mono<Response<ServiceInstance>> choose(Request request) {
  //    //注入的时候注入的是 Lazy Provider，这里取出真正的 Bean，也就是 ServiceInstanceListSupplier  
  ServiceInstanceListSupplier supplier = (ServiceInstanceListSupplier)this.serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
    //获取实例列表
    return supplier.get(request)
      .next()
      //从列表中选择一个实例
      .map((serviceInstances) -> {
        return this.processInstanceResponse(supplier, serviceInstances);
    });
}

private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier, List<ServiceInstance> serviceInstances) {
    Response<ServiceInstance> serviceInstanceResponse = this.getInstanceResponse(serviceInstances);
  	// 如果 ServiceInstanceListSupplier 也实现了 SelectedInstanceCallback，则执行下面的逻辑进行回调。SelectedInstanceCallback 就是每次负载均衡器选择实例之后进行的回调  
  if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
        ((SelectedInstanceCallback)supplier).selectedServiceInstance((ServiceInstance)serviceInstanceResponse.getServer());
    }

    return serviceInstanceResponse;
}

private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
    if (instances.isEmpty()) {
        if (log.isWarnEnabled()) {
            log.warn("No servers available for service: " + this.serviceId);
        }

        return new EmptyResponse();
    } else {
      //postion 原子 +1 并取绝对值
        int pos = Math.abs(this.position.incrementAndGet());
      //对列表大小取模, 返回对应下标的实例
        ServiceInstance instance = (ServiceInstance)instances.get(pos % instances.size());
        return new DefaultResponse(instance);
    }
}
```

`ServiceInstanceListSupplier` 是服务列表提供者接口：

**ServiceInstanceListSupplier**

```java
public interface ServiceInstanceListSupplier extends Supplier<Flux<List<ServiceInstance>>> {
    String getServiceId();

    default Flux<List<ServiceInstance>> get(Request request) {
        return (Flux)this.get();
    }

    static ServiceInstanceListSupplierBuilder builder() {
        return new ServiceInstanceListSupplierBuilder();
    }
}
```

spring-cloud-loadbalancer 中有很多 `ServiceInstanceListSupplier` 的实现，在默认配置中是通过属性配置指定实现的，这个配置项是`spring.cloud.loadbalancer.configurations`。例如：

**LoadBalancerClientConfiguration**

```java
        @Bean
        @ConditionalOnBean({DiscoveryClient.class})
        @ConditionalOnMissingBean
				//spring.cloud.loadbalancer.configurations 未指定或者为 default
        @ConditionalOnProperty(
            value = {"spring.cloud.loadbalancer.configurations"},
            havingValue = "default",
            matchIfMissing = true
        )
        public ServiceInstanceListSupplier discoveryClientServiceInstanceListSupplier(ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder()
              //通过 DiscoveryClient 提供实例
              .withBlockingDiscoveryClient()
              //开启缓存
              .withCaching()
              .build(context);
        }

        @Bean
        @ConditionalOnBean({DiscoveryClient.class})
        @ConditionalOnMissingBean
			//如果 spring.cloud.loadbalancer.configurations 指定为 zone-preference
        @ConditionalOnProperty(
            value = {"spring.cloud.loadbalancer.configurations"},
            havingValue = "zone-preference"
        )
        public ServiceInstanceListSupplier zonePreferenceDiscoveryClientServiceInstanceListSupplier(ConfigurableApplicationContext context) {
            return ServiceInstanceListSupplier.builder()
              //通过 DiscoveryClient 提供实例
              .withBlockingDiscoveryClient()
        			//启用更倾向于同一个 zone 下实例的特性
              .withZonePreference()
              //开启缓存
              .withCaching()
              .build(context);
        }
```

可以看到，可以通过 `ServiceInstanceListSupplier.builder()` 生成官方封装好各种特性的 `ServiceInstanceListSupplier`。其实从底层实现可以看出，所有的 `ServiceInstanceListSupplier` 实现都是代理模式，例如对于默认配置，底层代码近似于：

```java
if (cacheManagerProvider.getIfAvailable() != null) {
  //开启服务实例缓存  
  return new CachingServiceInstanceListSupplier(delegate, (CacheManager)cacheManagerProvider.getIfAvailable());
}
```

除了默认配置 `LoadBalancerClientConfiguration`，用户配置自定义配置则是通过 `@LoadBalancerClients` 和 `@LoadBalancerClient`.这个原理是通过 `LoadBalancerClientConfigurationRegistrar` 实现的。首先，我们来看一下 `LoadBalancerClientFactory` 这个 `NamedContextFactory` 是如何创建的：

[`LoadBalancerAutoConfiguration`]

```java
private final ObjectProvider<List<LoadBalancerClientSpecification>> configurations;

public LoadBalancerAutoConfiguration(ObjectProvider<List<LoadBalancerClientSpecification>> configurations) {
  //注入 LoadBalancerClientSpecification List 的 provider
  //在 Bean 创建的时候，进行载入，而不是注册的时候
  this.configurations = configurations;
}

@ConditionalOnMissingBean
@Bean
public LoadBalancerClientFactory loadBalancerClientFactory() {
  //创建 LoadBalancerClientFactory   
  LoadBalancerClientFactory clientFactory = new LoadBalancerClientFactory();
  //读取所有的 LoadBalancerClientSpecification，设置为 LoadBalancerClientFactory 的配置   
  clientFactory.setConfigurations((List)this.configurations.getIfAvailable(Collections::emptyList));
  return clientFactory;
}
```

那么，`LoadBalancerClientSpecification` 这些 Bean 是怎么创建的呢？在 `@LoadBalancerClients` 和 `@LoadBalancerClient` 注解中，都包含 `@Import(LoadBalancerClientConfigurationRegistrar.class)`。这个 `@Import` 加载一个 `ImportBeanDefinitionRegistrar`，这里是 `LoadBalancerClientConfigurationRegistrar`. `ImportBeanDefinitionRegistrar`里面的方法参数包含注解元数据，以及注册 Bean 的 `BeanDefinitionRegistry`。一般通过注解元数据，动态通过 `BeanDefinitionRegistry` 注册 Bean，在这里的实现是：

**[`LoadBalancerClients`]**

```java
@Configuration(
    proxyBeanMethods = false
)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({LoadBalancerClientConfigurationRegistrar.class})
public @interface LoadBalancerClients {
  //可以指定多个 LoadBalancerClient
    LoadBalancerClient[] value() default {};
	//指定所有的负载均衡配置的默认配置
    Class<?>[] defaultConfiguration() default {};
}
```

**[`LoadBalancerClient`]**

```java
@Configuration(
    proxyBeanMethods = false
)
@Import({LoadBalancerClientConfigurationRegistrar.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoadBalancerClient {
  //name 和 value 都是微服务名称
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";
	//这个微服务的配置
    Class<?>[] configuration() default {};
}
```

**[`LoadBalancerClientConfigurationRegistrar`]**

```java
public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
  //获取 LoadBalancerClients 注解的元数据  
  Map<String, Object> attrs = metadata.getAnnotationAttributes(LoadBalancerClients.class.getName(), true);
    if (attrs != null && attrs.containsKey("value")) {
      //对于 value 属性，其实就是一个 LoadBalancerClient 列表，对于每个生成一个特定微服务名字的  LoadBalancerClientSpecification  
      AnnotationAttributes[] clients = (AnnotationAttributes[])((AnnotationAttributes[])attrs.get("value"));
        AnnotationAttributes[] var5 = clients;
        int var6 = clients.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            AnnotationAttributes client = var5[var7];
            registerClientConfiguration(registry, getClientName(client), client.get("configuration"));
        }
    }
		//如果指定了 defaultConfiguration，则注册为 default 的配置
    if (attrs != null && attrs.containsKey("defaultConfiguration")) {
        String name;
        if (metadata.hasEnclosingClass()) {
            name = "default." + metadata.getEnclosingClassName();
        } else {
            name = "default." + metadata.getClassName();
        }

        registerClientConfiguration(registry, name, attrs.get("defaultConfiguration"));
    }
	//获取 LoadBalancerClient 注解的元数据
    Map<String, Object> client = metadata.getAnnotationAttributes(LoadBalancerClient.class.getName(), true);
    String name = getClientName(client);
    if (name != null) {
        registerClientConfiguration(registry, name, client.get("configuration"));
    }

}

private static void registerClientConfiguration(BeanDefinitionRegistry registry, Object name, Object configuration) {
  //初始化 LoadBalancerClientSpecification 的 BeanDefinition，用于注册一个 LoadBalancerClientSpecification Bean
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(LoadBalancerClientSpecification.class);
  //构造器参数      
  builder.addConstructorArgValue(name);
  builder.addConstructorArgValue(configuration);
  //注册 Bean
    registry.registerBeanDefinition(name + ".LoadBalancerClientSpecification", builder.getBeanDefinition());
    }
```

从代码中我们可以看出，通过使用 `@LoadBalancerClients` 和 `@LoadBalancerClient` 注解可以自动生成对应的 `LoadBalancerClientSpecification` 进而实现公共负载均衡配置或者特定某个微服务的负载均衡配置。