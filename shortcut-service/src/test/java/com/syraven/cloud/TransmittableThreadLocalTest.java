package com.syraven.cloud;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: TransmittableThreadLocalTest
 * @Description:
 * @Author syrobin
 * @Date 2022-01-06 4:27 PM
 * @Version V1.0
 */
public class TransmittableThreadLocalTest {

    public static void main(String[] args) throws InterruptedException {
        //测试ThreadLocal 是否可以进行线程传递，结果为null
        /*ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();
        stringThreadLocal.set("zxxc");

        Thread thread = new Thread(() ->{
            String s = stringThreadLocal.get();
            System.out.println(s);
        });
        thread.start();
        //解决线程之间的父子传递
        ThreadLocal<String> parentTraceid = new InheritableThreadLocal<>();
        parentTraceid.set("traceId:382123");
        Thread thread1 = new Thread(() ->{
            String s = parentTraceid.get();
            System.out.println(s);
        });
        thread1.start();*/

        //线程池测试
        ThreadLocal<String> localThreadLocal = new InheritableThreadLocal<>();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CountDownLatch countDownLatch2 = new CountDownLatch(1);
        localThreadLocal.set("我是主线程1");
        executorService.submit(() ->{
            System.out.printf("1Thread-%s%n",localThreadLocal.get());
            countDownLatch.countDown();
        });
        countDownLatch.await();
        System.out.printf("%s%n",localThreadLocal.get());
        System.out.println("-----------------");
        System.out.println("-----------------");
        localThreadLocal.set("我是主线程2");
        executorService.submit(() ->{
            System.out.printf("2Thread-%s%n",localThreadLocal.get());
            countDownLatch2.countDown();
        });
        countDownLatch2.await();
        System.out.printf("%s%n",localThreadLocal.get());
        //关闭线程池
        executorService.shutdown();



        System.out.println("-----------------");
        System.out.println("-----------------");

        //TTL实现
        TransmittableThreadLocal<String> transmittableThreadLocal = new TransmittableThreadLocal<>();
        ExecutorService executorService2 = Executors.newFixedThreadPool(1);
        //需要注意的是，使用TTL的时候，要想传递的值不出问题，线程池必须用TTL加一层代理
        ExecutorService ttlExecutorService = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(1));
        CountDownLatch countDownLatch3 = new CountDownLatch(1);
        CountDownLatch countDownLatch4 = new CountDownLatch(1);

        //采用TTL实现
        ThreadLocal tl = new TransmittableThreadLocal();
        tl.set("我是主线程1");
        executorService2.submit(() ->{
            System.out.printf("1Thread-%s%n",tl.get());
            countDownLatch3.countDown();
        });
        countDownLatch3.await();

        tl.set("我是主线程2");
        executorService2.submit(() ->{
            System.out.printf("2Thread-%s%n",tl.get());
            countDownLatch4.countDown();
        });
        countDownLatch4.await();
        executorService.shutdown();
    }
}
