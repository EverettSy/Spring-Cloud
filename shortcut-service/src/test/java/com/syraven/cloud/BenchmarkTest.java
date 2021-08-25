package com.syraven.cloud;

import com.syraven.cloud.service.UrlConvertService;
import lombok.SneakyThrows;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName
 * @Description: 性能测试
 * @Author syrobin
 * @Date 2021-08-25 10:55 上午
 * @Version V1.0
 **/
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class BenchmarkTest {

    private UrlConvertService urlConvertService;

    private OkHttpClient okHttpClient;

    public static final MediaType JSON = MediaType.get("application/x-www-form-urlencoded");


    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(BenchmarkTest.class.getName() + ".*")
                .warmupIterations(1)// 预热
                .warmupTime(TimeValue.seconds(1))
                .measurementIterations(10) //总共测试10轮
                .measurementTime(TimeValue.seconds(5))//每轮测试时长
                .forks(1) //创建几个进程来测试
                .threads(16) //线程数
                .build();
        new Runner(options).run();
    }


    /**
     * setup初始化容器的时候只执行一次
     */
    @Setup(Level.Trial)
    public void init() {
        ConfigurableApplicationContext context = SpringApplication.run(ShortCutApplication.class);
        okHttpClient = new OkHttpClient();
        urlConvertService = context.getBean(UrlConvertService.class);
    }

    //@Benchmark
    public void httpRequest() {
        try {
            RequestBody requestBody = new FormBody.Builder()
                    .add("url", "http://baidu.com" + Math.random())
                    .build();
            String repo = post("http://127.0.0.1:8202/covert", requestBody);
        } catch (Exception ignored) {
        }
    }

    @Benchmark
    public void serviceRequest() {
        urlConvertService.convertUrl("http://baidu.com/" + Math.random());
    }

    @SneakyThrows
    public String post(String url, RequestBody requestBody) {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }

    }
}
