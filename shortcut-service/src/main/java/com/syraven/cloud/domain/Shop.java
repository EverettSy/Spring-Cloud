package com.syraven.cloud.domain;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-03-10 4:18 PM
 */
public class Shop {

    private static final Random random = new Random();

    private String name;

    public Shop(String name){
        this.name = name;
    }

    public String getPrice(String product) {
        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];
        return String.format("%s:%.2f:%s", name, price, code);
    }



    /**
     * 计算价格
     * @param product
     * @return
     */
    private double calculatePrice(String product) {
        delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    /**
     * 延迟
     */
    public static void delay() {

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机延迟
     */
    public static void randomDelay() {
       int delay = 500 + random.nextInt(2000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Future<Double> getPriceAsync1(String product){
        // 创建CompletableFuture对象，它会包含计算的结果
        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        //在另一个线程中以异步方式执行计算
        new Thread(() ->{
            try {
                double price = calculatePrice(product);
                //  需长时间计算的任务结束并得出结果时，设置Future的返回值
                futurePrice.complete(price);
            } catch (Exception e) {
                //异常处理
                futurePrice.completeExceptionally(e);
            }
        }).start();
        return futurePrice;
    }

    public Future<Double> getPriceAsync(String product) {
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }
    public static final List<Shop> shops = Lists.newArrayList(
            new Shop("香蕉"),new Shop("苹果"));

    private static final Executor executor =
            Executors.newFixedThreadPool(Math.min(shops.size(),100), new ThreadFactory() {
                @Override
                public Thread newThread(@NotNull Runnable r) {
                    Thread thread = new Thread(r);
                    //设置守护进程---这种方式不会阻止程序关掉
                    thread.setDaemon(true);
                    return thread;
                }
            });



    private static List<String> findPrices_1(String product){

        List<CompletableFuture<String>>  priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getName() +" price is" + shop.getPrice(product)
                        , executor)).collect(Collectors.toList());
        return priceFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    private static List<String> findPrices(String product){
        List<CompletableFuture<String>> priceFutures = shops.stream()
                // 以异步方式取得每个shop中指定产品的原始价格
                .map(shop -> CompletableFuture
                        .supplyAsync(() -> shop.getPrice(product), executor))
                // Quote对象存在时，对其返回值进行转换
                .map(future -> future.thenApply(Quote::parse))
                // 使用另一个异步任务构造期望的Future，申请折扣
                .map(future -> future.thenCompose(quote -> CompletableFuture
                        .supplyAsync(() -> Discount.applyDiscount(quote),executor)))
                .collect(Collectors.toList());
        return priceFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());


    }

    public static void main1(String[] args) {
        Shop shop = new Shop("苹果");
        long start = System.nanoTime();
        //查询商店，试图取得商品的价格
        Future<Double> futurePrice = shop.getPriceAsync("ipad");
        long invocationTime = (System.nanoTime() - start) / 1_000_000;
        System.err.println("Invocation returned after "+ invocationTime+ " msecs");

        // 执行更多任务，比如查询其他商店
        //doSomethingElse();
        // 在计算商品价格的同时
        try {
            // 从Future对象中读取价格，如果价格未知，会发生阻塞
            double price = futurePrice.get();
            System.out.printf("Price is %.2f%n", price);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs");

    }


    // 这里演示获取最先返回的数据
    public static Stream<CompletableFuture<String>> findPricesStream(String product) {
        return shops.stream().map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(
                        () -> Discount.applyDiscount(quote), executor)));
    }

    public static void main(String[] args) {

        long start = System.nanoTime();
        //System.out.println(findPrices("myPhone27S"));
        CompletableFuture[] futures = findPricesStream("myPhone27S")
                .map(f -> f.thenAccept(s -> System.out.println(s + " (done in " +
                        ((System.nanoTime() - start) / 1_000_000) + " msecs)")))
                .toArray(CompletableFuture[]::new);
//        CompletableFuture.allOf(futures).join();
        CompletableFuture.anyOf(futures).join();
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");

    }

    public String getName() {
        return name;
    }
}
