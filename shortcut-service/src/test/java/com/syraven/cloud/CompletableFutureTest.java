package com.syraven.cloud;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: CompletableFutureTest
 * @Description: 异步编程测试
 * @Author syrobin
 * @Date 2021-11-23 11:02 上午
 * @Version V1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CompletableFutureTest {

    public static void main(String[] args) throws ExecutionException,InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("CompletableFuture 可以监控这个任务的执行");
                //任务完成返回结果
                future.complete("任务结果返回");
            }
        }).start();
        //获取任务结果
        System.out.println(future.get());

        //supplyAsync&runAsync的使用例子。
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println("executorService 是否为守护线程："+Thread.currentThread().isDaemon());
                return null;
            }
        });

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() ->{
            System.out.println("这是 lambda supplyAsync");
            System.out.println("supplyAsync 是否为守护线程" + Thread.currentThread().isDaemon());
            System.out.println("this lambda is executed by forkJoinPool");
            return "completableFutureResult";

        });

        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("this is task with executor");
            System.out.println("supplyAsync 使用executorService 时是否为守护线程 : " + Thread.currentThread().isDaemon());
            return "stringCompletableFuture";
        }, executorService);

        //System.out.println(completableFuture.get());
        //System.out.println(stringCompletableFuture.get());
        executorService.shutdown();


    }

}
