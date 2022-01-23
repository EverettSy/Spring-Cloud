接下来，我们开始分析 OpenFeign 的生命周期，结合 OpenFeign 本身的源代码。首先是从接口定义创建 OpenFeign 代理开始。我们这里只关心同步客户端，因为异步客户端目前还在实现中，并且在我们的项目中，异步响应式的客户端不用 OpenFeign，而是用的官方的 WebClient

# 创建 OpenFeign 代理

创建 OpenFeign 代理，主要分为以下几步：

1. 使用 Contract 解析接口的每一个方法，生成每一个方法的元数据列表：`List<MethodMetadata> metadata`
2. 根据每一个 `MethodMetadata`，生成对应的请求模板工厂 `RequestTemplate.Factory`，用于生成后面的请求。同时，使用这个模板工厂以及其他配置生成对应的方法处理器 `MethodHandler`，对于同步的 OpenFeign，`MethodHandler` 实现为 `SynchronousMethodHandler`。将接口方法与 `MethodHandler` 一一对应建立映射，结果为 `Map<Method, MethodHandler> methodToHandler`。对于 Java 8 引入的 interface default 方法，需要用不同 `MethodHandler`，即 `DefaultMethodHandler` ，因为这种方法不用代理，不用生成对应的 http 调用，其实现为直接调用对应的 default 方法代码。
3. 使用 InvocationHandlerFactory 这个工厂，创建 `InvocationHandler` 用于代理调用。
4. 调用 JDK 动态代理生成类方法使用 `InvocationHandler` 创建代理类。

创建 OpenFeign 代理，主要基于 JDK 的动态代理实现。我们先举一个简单的例子，创建一个 JDK 动态代理，用来类比。

## JDK 动态代理

使用 JDK 动态代理，需要如下几个步骤：

1. 编写接口以及对应的代理类。我们这里编写一个简单的接口和对应的实现类:

```java
public interface TestService {
    void test();
}
```

```java
public class TestServiceImpl implements TestService {
    @Override
    public void test() {
        System.out.println("TestServiceImpl#test is called");
    }
}
```

2.创建代理类实现`java.lang.reflect.InvocationHandler`，并且，在核心方法中，调用实际的对象，这里即我们上面 TestService 的实现类 TestServiceImpl 的对象。

JDK 中有内置的动态代理 API，其核心是 `java.lang.reflect.InvocationHandler`。我们先来创建一个简单的 `InvocationHandler` 实现类：

```java
public class SimplePrintMethodInvocationHandler implements InvocationHandler {
    private final TestService testService;
    public SimplePrintMethodInvocationHandler(TestService testService){
        this.testService = testService;
    }

    @Override
    public Object invoke(
            //代理对象
            Object proxy,
            //调用的方法
            Method method,
            //使用的参数
            Object[] args) throws Throwable {
        System.out.println("Invoked method: "+ method.getName());
        //进行实际的调用
        return method.invoke(testService,args);
    }
}
```

3.创建代理对象，并使用代理对象调用。一般通过 Proxy 的静态方法去创建，例如:

```java
//首先，创建要代理的对象
TestServiceImpl testServiceImpl = new TestServiceImpl();
//然后使用要代理的对象创建对应的 InvocationHandler
SimplePrintMethodInvocationHandler simplePrintMethodInvocationHandler = new SimplePrintMethodInvocationHandler(testServiceImpl);
//创建代理类，因为一个类可能实现多个接口，所以这里返回的是 Object，用户根据自己需要强制转换成要用的接口
Object proxyInstance = Proxy.newProxyInstance(
        TestService.class.getClassLoader(),
        testServiceImpl.getClass().getInterfaces(),
        simplePrintMethodInvocationHandler
);
//强制转换
TestService proxied = (TestService) proxyInstance;
//使用代理对象进行调用
proxied.test();
```

 

这样，我们就使用了 JDK 的内置动态代理机制实现了一个简单的动态代理。在 OpenFeign 的使用中，和我们的示例有一点区别。首先，我们只需要定义要代理的接口，不用定义实现类。因为所有的 OpenFeign 接口要做的事情其实都是 HTTP 调用，其信息可以自动从接口定义中生成，我们可以使用统一的对象根据接口定义，承载 OpenFeign 接口定义的请求。在 OpenFeign 中，这个等同于实现对象的，就是根据接口生成的 MethodHandler，在同步的 OpenFeign 中，即 `feign.SynchronousMethodHandler`。之后，OpenFeign 创建的 InvocationHandler，其实就是将调用转发到对应的 `SynchronousMethodHandler` 的对应方法。

## 创建 OpenFeign 代理对象的流程详解

我们使用前面的例子，来看一下创建代理的流程：

```java
public interface GitHub {

    /**
     * 定义get方法，包括路径参数，响应返回序列化类
     * @param owner
     * @param repository
     * @return
     */
    @RequestLine("GET /repos/{owner}/{repo}/contributors")
    List<Contributor> contributors(@Param("owner") String owner,
                                   @Param("repo") String repository);


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class Contributor {

        String login;
        int contributions;

    }
}

/**
 * 基于 FastJson 的反序列化解码器
 */
static class FastJsonDecoder implements Decoder {
    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        //读取 body
        byte[] body = response.body().asInputStream().readAllBytes();
        return JSON.parseObject(body, type);
    }
}
```

```java
public static void main(String[] args) {
    //创建 Feign 代理的 HTTP 调用接口实现
    GitHub github = Feign.builder()
                        //指定解码器为 FastJsonDecoder
                        .decoder(new FastJsonDecoder())
                        //指定代理类为 GitHub，基址为 https://api.github.com
                        .target(GitHub.class, "https://api.github.com");
    List<GitHub.Contributor> contributors = github.contributors("OpenFeign", "feign");
}
```

我们这里关心的其实就是创建 Feign 代理的 HTTP 调用接口实现这一步的内部流程。首先我们来看 Feign 的 Builder 的结构，当我们初始化一个 Feign 的 Builder 也就是调用 `Feign.builder()` 时，会创建如下组件（同时也说明以下组件都是可以配置的，如果一些配置之前没有提到，则可以）：

```java
//请求拦截器列表，默认为空
private final List<RequestInterceptor> requestInterceptors = new ArrayList();
//日志级别，默认不打印任何日志
private Level logLevel = Level.NONE;
//负责解析类元数据的 Contract，默认是支持 OpenFeign 内置注解的默认 Contract 
private Contract contract = new Contract.Default();
//承载 HTTP 请求的 Client，默认是基于 Java HttpURLConnection 的 Default Client
private Client client = new feign.Client.Default((SSLSocketFactory)null, (HostnameVerifier)null);
//重试器，默认也是 Default
private Retryer retryer = new feign.Retryer.Default();
//默认的日志 Logger，默认不记录任何日志
private Logger logger = new NoOpLogger();
//编码器解码器也是默认的
private Encoder encoder = new feign.codec.Encoder.Default();
private Decoder decoder = new feign.codec.Decoder.Default();
//查询参数编码，这个我们一般不会修改
private QueryMapEncoder queryMapEncoder = new FieldQueryMapEncoder();
//错误编码器，默认为 Default
private ErrorDecoder errorDecoder = new feign.codec.ErrorDecoder.Default();
//各种超时的 Options 走的默认配置
private Options options = new Options();
//用来生成 InvocationHandler 的 Factory 也是默认的
private InvocationHandlerFactory invocationHandlerFactory = new feign.InvocationHandlerFactory.Default();
//是否特殊解析 404 错误，因为针对 404 我们可能不想抛出异常，默认是 false
private boolean decode404 = false;
//是否在解码后立刻关闭 Response，默认为是
private boolean closeAfterDecode = true;
//异常传播规则，默认是不传播
private ExceptionPropagationPolicy propagationPolicy = ExceptionPropagationPolicy.NONE;
//是否强制解码，这个主要为了兼容异步 Feign 引入的配置，我们直接忽略，认为他就是 false 即可
private boolean forceDecoding = false;
private List<Capability> capabilities = new ArrayList();
```

我们的代码中指定了指定解码器为 FastJsonDecoder，所以 Decoder 就是 FastJsonDecoder 了。最后通过 `target(GitHub.class, "https://api.github.com");`指定定代理类为 GitHub，基址为 https://api.github.com，这时候就会生成 Feign 代理类，其步骤是：

```java
public <T> T target(Class<T> apiType, String url) {
  //使用代理接口类型，以及基址创建 HardCodedTarget，他的意思其实就是硬编码的 Target
  return target(new HardCodedTarget<T>(apiType, url));
}

public <T> T target(Target<T> target) {
  return build().newInstance(target);
}

public Feign build() {
  //将所有组件经过所有的 Capability，从这里我们可以看出，我们可以实现 Capability 接口来在创建 Feign 代理的时候动态修改组件
  Client client = Capability.enrich(this.client, capabilities);
  Retryer retryer = Capability.enrich(this.retryer, capabilities);
  List<RequestInterceptor> requestInterceptors = this.requestInterceptors.stream()
      .map(ri -> Capability.enrich(ri, capabilities))
      .collect(Collectors.toList());
  Logger logger = Capability.enrich(this.logger, capabilities);
  Contract contract = Capability.enrich(this.contract, capabilities);
  Options options = Capability.enrich(this.options, capabilities);
  Encoder encoder = Capability.enrich(this.encoder, capabilities);
  Decoder decoder = Capability.enrich(this.decoder, capabilities);
  InvocationHandlerFactory invocationHandlerFactory =
      Capability.enrich(this.invocationHandlerFactory, capabilities);
  QueryMapEncoder queryMapEncoder = Capability.enrich(this.queryMapEncoder, capabilities);

  //创建 SynchronousMethodHandler 的 Factory，用于生成 SynchronousMethodHandler，SynchronousMethodHandler 是实际承载 Feign 代理请求的实现类
  SynchronousMethodHandler.Factory synchronousMethodHandlerFactory =
      new SynchronousMethodHandler.Factory(client, retryer, requestInterceptors, logger,
          logLevel, decode404, closeAfterDecode, propagationPolicy, forceDecoding);
  //通过方法名称来区分不同接口方法的元数据解析，用于生成并路由到对应的代理方法
  ParseHandlersByName handlersByName =
      new ParseHandlersByName(contract, options, encoder, decoder, queryMapEncoder,
          errorDecoder, synchronousMethodHandlerFactory);
  //创建 ReflectiveFeign
  return new ReflectiveFeign(handlersByName, invocationHandlerFactory, queryMapEncoder);
}
}
```

创建 ReflectiveFeign 之后，会调用其中的 newInstance 方法：

ReflectiveFeign

```java
public <T> T newInstance(Target<T> target) {
    //使用前面提到的 ParseHandlersByName 解析元数据并生成所有需要代理的方法的 MethodHandler，我们这里分析的是同步 Feign，所以是 SynchronousMethodHandler
    Map<String, MethodHandler> nameToHandler = targetToHandlersByName.apply(target);
    Map<Method, MethodHandler> methodToHandler = new LinkedHashMap<Method, MethodHandler>();
    List<DefaultMethodHandler> defaultMethodHandlers = new LinkedList<DefaultMethodHandler>();

    //将方法与对应的 MethodHandler 一一对应
    for (Method method : target.type().getMethods()) {
      if (method.getDeclaringClass() == Object.class) {
        //对于 Object 的方法，直接跳过
        continue;
      } else if (Util.isDefault(method)) {
        //如果是 java 8 中接口的默认方法，就使用 DefaultMethodHandler 处理
        DefaultMethodHandler handler = new DefaultMethodHandler(method);
        defaultMethodHandlers.add(handler);
        methodToHandler.put(method, handler);
      } else {
        methodToHandler.put(method, nameToHandler.get(Feign.configKey(target.type(), method)));
      }
    }
    //使用前面 Builder 中的 InvocationHandlerFactory 创建 InvocationHandler
    InvocationHandler handler = factory.create(target, methodToHandler);
    //使用 InvocationHandler 创建 Proxy
    T proxy = (T) Proxy.newProxyInstance(target.type().getClassLoader(),
        new Class<?>[] {target.type()}, handler);
    //将代理与 DefaultMethodHandler 关联
    for (DefaultMethodHandler defaultMethodHandler : defaultMethodHandlers) {
      defaultMethodHandler.bindTo(proxy);
    }
    return proxy;
  }
```

对于使用前面提到的 ParseHandlersByName 解析元数据并生成所有需要代理的方法的 MethodHandler 这一步，主要就涉及到了使用 Contract 解析出方法的元数据，然后将这些元数据用对应的编码器绑定用于之后调用的编码：

ReflectiveFeign

```java
public Map<String, MethodHandler> apply(Target target) {
      // 使用 Contract 解析出所有方法的元数据
      List<MethodMetadata> metadata = contract.parseAndValidateMetadata(target.type());
      Map<String, MethodHandler> result = new LinkedHashMap<String, MethodHandler>();
      // 对于每个解析出的方法元数据
      for (MethodMetadata md : metadata) {
        BuildTemplateByResolvingArgs buildTemplate;
        if (!md.formParams().isEmpty() && md.template().bodyTemplate() == null) {
          //有表单的情况
          buildTemplate =
              new BuildFormEncodedTemplateFromArgs(md, encoder, queryMapEncoder, target);
        } else if (md.bodyIndex() != null) {
          //有 body 的情况
          buildTemplate = new BuildEncodedTemplateFromArgs(md, encoder, queryMapEncoder, target);
        } else {
          //其他情况
          buildTemplate = new BuildTemplateByResolvingArgs(md, queryMapEncoder, target);
        }
        if (md.isIgnored()) {
          result.put(md.configKey(), args -> {
            throw new IllegalStateException(md.configKey() + " is not a method handled by feign");
          });
        } else {
          // 使用 SynchronousMethodHandler 的 Factory 生成 SynchronousMethodHandler 
          result.put(md.configKey(),
              factory.create(target, md, buildTemplate, options, decoder, errorDecoder));
        }
      }
      return result;
    }
  }
```

默认的 InvocationHandlerFactory 生成的 InvocationHandler 是 ReflectiveFeign.FeignInvocationHandler：

```java
static final class Default implements InvocationHandlerFactory {
    @Override
    public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
      return new ReflectiveFeign.FeignInvocationHandler(target, dispatch);
    }
}
```

其中的内容是：

```java
@Override
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
  // 对于 equals，hashCode，toString 方法直接调用
  if ("equals".equals(method.getName())) {
    try {
      Object otherHandler =
          args.length > 0 && args[0] != null ? Proxy.getInvocationHandler(args[0]) : null;
      return equals(otherHandler);
    } catch (IllegalArgumentException e) {
      return false;
    }
  } else if ("hashCode".equals(method.getName())) {
    return hashCode();
  } else if ("toString".equals(method.getName())) {
    return toString();
  }
  //对于其他方法，调用对应的 SynchronousMethodHandler 进行处理
  return dispatch.get(method).invoke(args);
}
```

从这里我们就可以看出，我们生成的 Proxy，其实就是将请求代理到了 SynchronousMethodHandler 上。

## 总结和后续

我们这一节详细介绍了 OpenFeign 创建代理的详细流程，可以看出，对于同步 Feign 生成的 Proxy，其实就是将接口 HTTP 请求定义的方法请求代理到了 SynchronousMethodHandler 上。下一节我们会详细分析 SynchronousMethodHandler 做实际 HTTP 调用的流程，来搞清楚 Feign 所有组件是如何协调工作的。