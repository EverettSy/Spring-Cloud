package com.syraven.cloud;

import com.syraven.cloud.codec.FastJsonDecoder;
import com.syraven.cloud.codec.TestErrorDecoder;
import com.syraven.cloud.handler.SimplePrintMethodInvocationHandler;
import com.syraven.cloud.service.GitHub;
import com.syraven.cloud.service.TestHttpBin;
import com.syraven.cloud.service.TestService;
import com.syraven.cloud.service.impl.TestServiceImpl;
import feign.ExceptionPropagationPolicy;
import feign.Feign;
import feign.Request;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: FeignTest
 * @Description:
 * @Author syrobin
 * @Date 2021-11-16 5:33 下午
 * @Version V1.0
 */
public class FeginTest extends SpringTest{


    public static void main(String[] args) {

        /*HttpBin httpBin = Feign.builder()
                .contract(new CustomizedContract())
                .target(HttpBin.class, "http://www.httpbin.org");
        //实际上就是调用 http://www.httpbin.org/get
        String s = httpBin.get();
        System.out.println(s);*/

        //编码与解码
        /*HttpBin httpBin = Feign.builder()
                .decoder(new FastJsonDecoder())
                .encoder(new FastJsonEncoder())
                .target(HttpBin.class, "http://www.httpbin.org");
        //实际上就是调用 http://www.httpbin.org/anything
        Map<String, String> map = Maps.newHashMap();
        map.put("key","value");
        Object o = httpBin.postBody(map);
        System.out.println(o);*/

        //请求拦截器
        /*HttpBin httpBin = Feign.builder()
                .requestInterceptor(new AddHeaderRequestInterceptor())
                .target(HttpBin.class, "http://www.httpbin.org");
        String s = httpBin.anything();
        System.out.println(s);*/

        //错误解码器相关
        TestHttpBin httpBin = Feign.builder()
                //连接超时为 500ms，读取超时为 6s，跟随重定向的 Feign
                .options(new Request.Options(500,
                        TimeUnit.MILLISECONDS,6,TimeUnit.SECONDS,true))
                .errorDecoder(new TestErrorDecoder())
                //如果这里没有指定为 UNWRAP 那么下面抛出的异常就是 RetryableException，否则就是 RetryableException 的 cause 也就是 FeignException
                .exceptionPropagationPolicy(ExceptionPropagationPolicy.UNWRAP)
                .target(TestHttpBin.class, "http://httpbin.org");
        Object o = httpBin.get();
        System.out.println(o);



        //首先，创建要代理的对象
        TestServiceImpl testServiceImpl = new TestServiceImpl();
        //然后使用要代理的对象创建对应的 InvocationHandler
        SimplePrintMethodInvocationHandler simplePrintMethodInvocationHandler =
                new SimplePrintMethodInvocationHandler(testServiceImpl);
        //创建代理类，因为一个类可能实现多个接口，所以这里返回的是 Object，用户根据自己需要强制转换成要用的接口
        Object proxyInstance = Proxy.newProxyInstance(TestService.class.getClassLoader(),
                testServiceImpl.getClass().getInterfaces(),
                simplePrintMethodInvocationHandler);
        //强制转换
        TestService proxied = (TestService) proxyInstance;
        //使用代理对象调用
        proxied.test();

        //创建 Feign 代理的 HTTP 调用接口实现
        GitHub gitHub = Feign.builder()
                //指定解码器未 FastJsonDecoder
                .decoder(new FastJsonDecoder())
                //指定代理类为 GitHub，基址为 https://api.github.com
                .target(GitHub.class,"http://api.github.com");
        List<GitHub.Contributor> contributors = gitHub.contributors("OpenFeign","feign");





    }



}
