# Spring Cloud系列之OpenFeign组件
首先，我们给出官方文档中的组件结构图：
![](https://camo.githubusercontent.com/f1bd8b9bfe3c049484b0776b42668bb76a57872fe0f01402e5ef73d29b811e50/687474703a2f2f7777772e706c616e74756d6c2e636f6d2f706c616e74756d6c2f70726f78793f63616368653d6e6f267372633d68747470733a2f2f7261772e67697468756275736572636f6e74656e742e636f6d2f4f70656e466569676e2f666569676e2f6d61737465722f7372632f646f63732f6f766572766965772d6d696e646d61702e69756d6c)

官方文档中的组件，是以实现功能为维度的，我们这里是以源码实现为维度的（因为之后我们使用的时候，需要根据需要定制这些组件，所以需要从源码角度去拆分分析），可能会有一些小差异。

### 负责解析类元数据的 Contract

OpenFeign 是通过代理类元数据来自动生成 HTTP API 的，那么到底解析哪些类元数据，哪些类元数据是有效的，是通过指定 Contract 来实现的，我们可以通过实现这个 Contract 来自定义一些类元数据的解析，例如，我们自定义一个注解：

```java
//仅可用于方法上
@java.lang.annotation.Target(METHOD)
//指定注解保持到运行时
@Retention(RUNTIME)
@interface Get {
    //请求 uri
    String uri();
}
```

这个注解很简单，标注了这个注解的方法会被自动封装成 GET 请求，请求 uri 为 `uri()` 的返回。

然后，我们自定义一个 Contract 来处理这个注解。由于 `MethodMetadata` 是 final 并且是 package private 的，所以我们只能继承 `Contract.BaseContract` 去自定义注解解析：

```java
package com.syraven.cloud.contract;

import com.syraven.cloud.annotation.Get;
import feign.Contract;
import feign.MethodMetadata;
import feign.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @ClassName: CustomizedContract
 * @Description: 自定注解 Contract
 * 外部自定义必须继承 BaseContract，因为里面生成的 MethodMetadata 的构造器是 package private 的
 * @Author syrobin
 * @Date 2021-12-29 4:59 PM
 * @Version V1.0
 */
public class CustomizedContract extends Contract.BaseContract {
    @Override
    protected void processAnnotationOnClass(MethodMetadata data, Class<?> clz) {
        //处理类上面的注解，这里没用到

    }

    @Override
    protected void processAnnotationOnMethod(MethodMetadata data, Annotation annotation, Method method) {
        //处理方法上的注解
        Get get = method.getAnnotation(Get.class);
        //如果Get 注解存在，则指定方法 HTTP 请求方式为GET,同时uri 指定为注解uri()的返回
        if (get != null){
            data.template().method(Request.HttpMethod.GET);
            data.template().uri(get.uri());
        }
    }

    @Override
    protected boolean processAnnotationsOnParameter(MethodMetadata data, Annotation[] annotations, int paramIndex) {
        //处理参数上面的注解
        return false;
    }
}
```

然后，我们来使用这个 Contract：

```java
public interface HttpBin {

    @Get(uri = "/get")
    String get();
}

HttpBin httpBin = Feign.builder()
                .contract(new CustomizedContract())
                .target(HttpBin.class, "http://www.httpbin.org");
        //实际上就是调用 http://www.httpbin.org/get
        String s = httpBin.get();
        System.out.println(s);
```

一般的，我们不会使用这个 Contract，因为我们业务上一般不会自定义注解。这是底层框架需要用的功能。比如在 spring-mvc 环境下，我们需要兼容 spring-mvc 的注解，这个实现类就是 `SpringMvcContract`。

### 编码器 Encoder 与解码器 Decoder

编码器与解码器接口定义：

```java
public interface Decoder {
  Object decode(Response response, Type type) throws IOException, DecodeException, FeignException;
}
public interface Encoder {
  void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException;
}
```

OpenFeign 可以自定义编码解码器，我们这里使用 FastJson 自定义实现一组编码与解码器，来了解其中使用的原理。

```java
/**
 * 基于 FastJson 的反序列化解码器
 */
public class FastJsonDecoder implements Decoder {
    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        //读取body
        byte[] body = Util.toByteArray(response.body().asInputStream());
        return JSON.parseObject(body, type);
    }
}

/**
 * 基于 FastJson 的序列化编码器
 */
public class FastJsonEncoder implements Encoder {
    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        if (object != null){
            //编码body
            template.header(CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
            template.body(JSON.toJSONBytes(object), StandardCharsets.UTF_8);
        }
    }
}
```

然后，我们通过 `http://httpbin.org/anything` 来测试，这个链接会返回我们发送的请求的一切元素。

```java
public interface HttpBin {

    @RequestLine("POST /anything")
    Object postBody(Map<String,String> body);
}

HttpBin httpBin = Feign.builder()
                .decoder(new FastJsonDecoder())
                .encoder(new FastJsonEncoder())
                .target(HttpBin.class, "http://www.httpbin.org");
        //实际上就是调用 http://www.httpbin.org/anything
        Map<String, String> map = Maps.newHashMap();
        map.put("key","value");
        Object o = httpBin.postBody(map);
        System.out.println(o);
```

查看响应，可以看到我们发送的 json body 被正确的接收到了。

目前，OpenFeign 项目中的编码器以及解码器主要实现包括：

| 序列化                             | 需要额外添加的依赖 | 实现类                                                       |
| ---------------------------------- | ------------------ | ------------------------------------------------------------ |
| 直接转换成字符串，默认的编码解码器 | 无                 | `feign.codec.Encoder.Default` 和 `feign.codec.Decoder.Default` |
| gson                               | feign-gson         | `feign.gson.GsonEncoder` 和 `feign.gson.GsonDecoder`         |
| xml                                | feign-jaxb         | `feign.jaxb.JAXBEncoder` 和 `feign.jaxb.JAXBDecoder`         |
| json (jackson)                     | feign-jackson      | `feign.jackson.JacksonEncoder` 和 `feign.jackson.JacksonDecoder` |

我们在 Spring Cloud 环境中使用的时候，在 Spring MVC 中是有统一的编码器以及解码器的，即 `HttpMessageConverters`，并且通过胶水项目做了兼容，所以我们统一用 `HttpMessageConverters` 指定自定义编码解码器就好。

### 请求拦截器 RequestInterceptor

RequestInterceptor 的接口定义：

```java
public interface RequestInterceptor {
  void apply(RequestTemplate template);
}
```

可以从接口看出，RequestInterceptor 其实就是对于 RequestTemplate 进行额外的操作。对于每次请求，都会经过所有的 RequestInterceptor 处理。

举个例子，我们可以对于每个请求加上特定的 Header：

```java
public interface HttpBin {

    //发到这个链接的所有请求，响应会返回请求中的所有元素
    @RequestLine("GET /anything")
    String anything();
}

public class AddHeaderRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        //添加header
        template.header("test-header","test-value");
    }
}

public static void main(String[] args) {
    HttpBin httpBin = Feign.builder()
            .requestInterceptor(new AddHeaderRequestInterceptor())
            .target(HttpBin.class, "http://www.httpbin.org");
    String s = httpBin.anything();
}

```

执行程序，可以在响应中看到我们发送请求中添加的 header。

### Http 请求客户端 Client

OpenFeign 底层的 Http 请求客户端是可以自定义的，OpenFeign 针对不同的 Http 客户端都有封装，默认的是通过 Java 内置的 Http 请求 API。我们来看下 Client 的接口定义源码：

```java
public interface Client {
  /**
   * 执行请求
   * @param request HTTP 请求
   * @param options 配置选项
   * @return
   * @throws IOException
   */
  Response execute(Request request, Options options) throws IOException;
}
```

Request 是 feign 中对于 Http 请求的定义，Client 的实现需要将 Request 转换成对应底层的 Http 客户端的请求并调用合适的方法进行请求。Options 是一些请求通用配置，包括：

```java
public static class Options {
    //tcp 建立连接超时
    private final long connectTimeout;
    //tcp 建立连接超时时间单位
    private final TimeUnit connectTimeoutUnit;
    //请求读取响应超时
    private final long readTimeout;
    //请求读取响应超时时间单位
    private final TimeUnit readTimeoutUnit;
    //是否跟随重定向
    private final boolean followRedirects;
}
```

目前，Client 的实现包括以下这些：

| 底层 HTTP 客户端       | 需要添加的依赖         | 实现类                                    |
| ---------------------- | ---------------------- | ----------------------------------------- |
| Java HttpURLConnection | 无                     | `feign.Client.Default`                    |
| Java 11 HttpClient     | feign-java11           | `feign.http2client.Http2Client`           |
| Apache HttpClient      | feign-httpclient       | `feign.httpclient.ApacheHttpClient`       |
| Apache HttpClient 5    | feign-hc5              | `feign.hc5.ApacheHttp5Client`             |
| Google HTTP Client     | feign-googlehttpclient | `feign.googlehttpclient.GoogleHttpClient` |
| Google HTTP Client     | feign-googlehttpclient | `feign.googlehttpclient.GoogleHttpClient` |
| jaxRS                  | feign-jaxrs2           | `feign.jaxrs2.JAXRSClient`                |
| OkHttp                 | feign-okhttp           | `feign.okhttp.OkHttpClient`               |
| Ribbon                 | feign-ribbon           | `feign.ribbon.RibbonClient`               |

### 错误解码器相关

可以指定错误解码器 `ErrorDecoder`，同时还可以指定异常抛出策略 `ExceptionPropagationPolicy`.

`ErrorDecoder` 是读取 HTTP 响应判断是否有错误需要抛出异常使用的：

```java
public interface ErrorDecoder {
    public Exception decode(String methodKey, Response response);
}
```

只有响应码不为 2xx 的时候，才会调用配置的 `ErrorDecoder` 的 `decode` 方法。默认的 `ErrorDecoder` 的实现是：

```java
public static class Default implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
      //将不同响应码包装成不同的异常
      FeignException exception = errorStatus(methodKey, response);
      //提取 Retry-After 这个 HTTP 响应头，如果存在这个响应头则将异常封装为 RetryableException
      //对于 RetryableException，在后面的分析我们会知道如果抛出这个异常会触发重试器的重试
      Date retryAfter = retryAfterDecoder.apply(firstOrNull(response.headers(), RETRY_AFTER));
      if (retryAfter != null) {
        return new RetryableException(
            response.status(),
            exception.getMessage(),
            response.request().httpMethod(),
            exception,
            retryAfter,
            response.request());
      }
      return exception;
    }
  }
```

可以看出， ErrorDecoder 是可能给异常封装一层异常的，这有时候对于我们在外层捕捉会造成影响，所以可以通过指定 `ExceptionPropagationPolicy` 来拆开这层封装。`ExceptionPropagationPolicy` 是一个枚举类：

```java
public enum ExceptionPropagationPolicy {
  //什么都不做
  NONE, 
  //是否将 RetryableException 的原始 exception 提取出来作为异常抛出
  //目前只针对 RetryableException 生效，调用 exception 的 getCause，如果不为空就返回这个 cause，否则返回原始 exception
  UNWRAP,
  ;
}
```

接下来看个例子：

```java
public interface TestHttpBin {

    //请求一定会返回 500
    @RequestLine("GET /status/500")
    Object get();
}

public class TestErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        //获取错误码对应的 FeignException
        FeignException exception = FeignException.errorStatus(methodKey, response);
        //封装为 RetryableException
        return new RetryableException(
                response.status(),
                exception.getMessage(),
                response.request().httpMethod(),
                exception,
                new Date(),
                response.request()
        );
    }
}


public static void main(String[] args) {
TestHttpBin httpBin = Feign.builder()
                .errorDecoder(new TestErrorDecoder())
                //如果这里没有指定为 UNWRAP 那么下面抛出的异常就是 RetryableException，否则就是 RetryableException 的 cause 也就是 FeignException
                .exceptionPropagationPolicy(ExceptionPropagationPolicy.UNWRAP)
                .target(TestHttpBin.class, "http://httpbin.org");
        Object o = httpBin.get();
        System.out.println(o);
}
```

执行后可以发现抛出了 `feign.FeignException$InternalServerError: [500 INTERNAL SERVER ERROR] during [GET] to [http://httpbin.org/status/500] [TestHttpBin#get()]: []` 这个异常。

## 针对 RetryableException 的重试器 Retryer

在调用发生异常的时候，我们可能希望按照一定策略进行重试，抽象这种重试策略一般包括：

- 对于哪些异常会重试
- 什么时候重试，什么时候结束重试，例如重试 n 次以后

对于那些异常会重试，这个由 ErrorDecoder 决定。如果异常需要被重试，就把它封装成 `RetryableException`，这样 Feign 就会使用 Retryer 进行重试。对于什么时候重试，什么时候结束重试，这些就是 Retryer 需要考虑的事情：

```java
public interface Retryer extends Cloneable {
  /**
    * 判断继续重试，或者抛出异常结束重试
    */
  void continueOrPropagate(RetryableException e);
  /**
    * 对于每次请求，都会调用这个方法创建一个新的同样配置的 Retryer 对象
    */
  Retryer clone();
}
```

我们来看一下 Retryer 的默认实现：

```java
class Default implements Retryer {
	//最大重试次数
    private final int maxAttempts;
	//初始重试间隔
    private final long period;
	//最大重试间隔
    private final long maxPeriod;
	//当前重试次数
    int attempt;
	//当前已经等待的重试间隔时间和
    long sleptForMillis;

    public Default() {
	  //默认配置，初始重试间隔为 100ms，最大重试间隔为 1s，最大重试次数为 5
      this(100, SECONDS.toMillis(1), 5);
    }

    public Default(long period, long maxPeriod, int maxAttempts) {
      this.period = period;
      this.maxPeriod = maxPeriod;
      this.maxAttempts = maxAttempts;
	  //当前重试次数从 1 开始，因为第一次进入 continueOrPropagate 之前就已经发生调用但是失败了并抛出了 RetryableException
      this.attempt = 1;
    }
  
  // visible for testing;
    protected long currentTimeMillis() {
      return System.currentTimeMillis();
    }

    public void continueOrPropagate(RetryableException e) {
	  //如果当前重试次数大于最大重试次数则
      if (attempt++ >= maxAttempts) {
        throw e;
      }

      long interval;
	  //如果指定了 retry-after，则以这个 header 为准决定等待时间
      if (e.retryAfter() != null) {
        interval = e.retryAfter().getTime() - currentTimeMillis();
        if (interval > maxPeriod) {
          interval = maxPeriod;
        }
        if (interval < 0) {
          return;
        }
      } else {
		//否则，通过 nextMaxInterval 计算
        interval = nextMaxInterval();
      }
      try {
        Thread.sleep(interval);
      } catch (InterruptedException ignored) {
        Thread.currentThread().interrupt();
        throw e;
      }
	  //记录一共等待的时间
      sleptForMillis += interval;
    }
  
  //每次重试间隔增长 50%，直到最大重试间隔
    long nextMaxInterval() {
      long interval = (long) (period * Math.pow(1.5, attempt - 1));
      return interval > maxPeriod ? maxPeriod : interval;
    }

    @Override
    public Retryer clone() {
	  //复制配置
      return new Default(period, maxPeriod, maxAttempts);
    }
  
```

默认的 Retryer 功能也比较丰富，用户可以参考这个实现更适合自己业务场景的重试器。

## 每个 HTTP 请求的配置 Options

无论是哪种 HTTP 客户端，都需要如下几个配置：

- 连接超时：这个是 TCP 连接建立超时时间
- 读取超时：这个是收到 HTTP 响应之前的超时时间
- 是否跟随重定向 OpenFeign 可以通过 Options 进行配置：

```java
public static class Options {

  private final long connectTimeout;
  private final TimeUnit connectTimeoutUnit;
  private final long readTimeout;
  private final TimeUnit readTimeoutUnit;
  private final boolean followRedirects;
}
```

例如我们可以这么配置一个连接超时为 500ms，读取超时为 6s，跟随重定向的 Feign：

```java
Feign.builder().options(new Request.Options(
    500, TimeUnit.MILLISECONDS, 6, TimeUnit.SECONDS, true
))
```

我们这一节详细介绍了 OpenFeign 的各个组件，有了这些知识，其实我们自己就能实现 Spring-Cloud-OpenFeign 里面的胶水代码。其实 Spring-Cloud-OpenFeign 就是将这些组件以 Bean 的形式注册到 NamedContextFactory 中，供不同微服务进行不同的配置。
