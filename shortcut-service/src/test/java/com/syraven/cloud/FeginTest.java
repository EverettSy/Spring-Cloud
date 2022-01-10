package com.syraven.cloud;

import com.syraven.cloud.codec.TestErrorDecoder;
import com.syraven.cloud.service.TestHttpBin;
import feign.ExceptionPropagationPolicy;
import feign.Feign;
import feign.Request;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName: FeignTest
 * @Description:
 * @Author syrobin
 * @Date 2021-11-16 5:33 下午
 * @Version V1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FeginTest {


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

    }



}
