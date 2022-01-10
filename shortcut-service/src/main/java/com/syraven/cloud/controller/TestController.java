package com.syraven.cloud.controller;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import com.syraven.cloud.spring.context.bean.ChildBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: TestController
 * @Description:
 * @Author syrobin
 * @Date 2021-10-18 2:09 下午
 * @Version V1.0
 */
@RestController
public class TestController {

    //线程池传递变量
    public static final ThreadLocal<Integer> transmittableThreadLocal = new TransmittableThreadLocal<>();
    public static final ThreadLocal<Map<Integer, Integer>> transmittableThreadLocal2 = new TransmittableThreadLocal<>();
    BlockingQueue workQueue;
    ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(1, 1, 600L, TimeUnit.SECONDS
                    , new LinkedBlockingQueue<>());
    int i = 0;

    @Autowired
    private ChildBean childBean;

    @RequestMapping("/test")
    public ChildBean getChildBean() {
        return childBean;
    }

    @GetMapping("/test/ttl")
    public String test() {
        transmittableThreadLocal.set(++i);
        System.out.println("主线程set：" + i);
        threadPoolExecutor.execute(Objects.requireNonNull(TtlRunnable.get(() -> {
            try {
                Thread.sleep(3L * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("子线程取到" + transmittableThreadLocal.get());
        })));
        transmittableThreadLocal.remove();
        System.out.println("主线程结束");
        return "success";
    }


    @GetMapping("/test/ttl2")
    public String test2() {
        Map<Integer, Integer> map = transmittableThreadLocal2.get();
        if (map == null) {
            init();
        }
        System.out.println("主线程开始："+ transmittableThreadLocal2.get());
        transmittableThreadLocal2.get().put(++i,i);
        System.out.println("主线程put："+ i +"="+i);
        threadPoolExecutor.execute(Objects.requireNonNull(TtlRunnable.get(() ->{
            try {
                Thread.sleep(3L * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("子线程取到"+ transmittableThreadLocal2.get());
        })));
        transmittableThreadLocal2.remove();
        System.out.println("主线程remove"+ transmittableThreadLocal2.get());
        System.out.println("主线程结束");
        return "success";
    }

    public static synchronized void init() {
        System.out.println("init bean");
        Map<Integer, Integer> map = transmittableThreadLocal2.get();
        if (map == null) {
            LinkedHashMap<Integer, Integer> linkedHashMap = new LinkedHashMap<>();
            transmittableThreadLocal2.set(linkedHashMap);
        }
    }

}
