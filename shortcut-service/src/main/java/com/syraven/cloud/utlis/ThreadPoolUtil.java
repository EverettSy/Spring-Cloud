package com.syraven.cloud.utlis;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author syrobin
 * @version v1.0
 * @description: 带阻塞队列的线程池
 * @date 2022-03-16 16:13
 */
public class ThreadPoolUtil {

    public static ThreadPoolExecutor getPool() {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                10, 10, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        return threadPool;
    }

    public static ThreadPoolExecutor getPool(int corePoolSize, int maxPoolSize, int capacity) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                corePoolSize, maxPoolSize, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(capacity),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        return threadPool;
    }
}
