package com.syraven.cloud;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author syrobin
 * @version v1.0
 * @description: 异步任务
 * @date 2022-03-10 3:19 PM
 */
public class FutureTaskTest {

    public static class CallImpl implements Callable<String> {

        private String input;

        public CallImpl(String input) {
            this.input = input;
        }

        @Override
        public String call() {
            Random random = new Random();
            try {
                Thread.sleep((random.nextInt() + this.input.hashCode()) & 10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "输入参数：" + input;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        @SuppressWarnings("unchecked")
        List<Future<String>> list = Arrays.asList(
                executorService.submit(new CallImpl("t1")),
                executorService.submit(new CallImpl("t2")),
                executorService.submit(new CallImpl("t3"))

        );

        for (Future<String> future : list) {
            String result = future.get();//如果没有返回，会阻塞
            System.err.println(result+"\t"+System.currentTimeMillis());
        }
        executorService.shutdown();

    }
}
