package com.syraven.cloud;

import com.google.common.base.Stopwatch;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author syrobin
 * @version v1.0
 * @description: 用优先队列实现TopN问题
 * @date 2022-05-20 10:55
 */
public class TopN {

    //需要得到的最大数字的数量
    //就是TopN中的N
    private int topNum;

    //用于实现TopN的优先队列
    private PriorityQueue<Long> priorityQueue;

    /**
     * TopN工具类的构造方法
     *
     * @param topNum 需要统计的最大数字的个数
     */
    public TopN(int topNum) {
        this.topNum = topNum;
        this.priorityQueue = new PriorityQueue<>(topNum);
    }

    /**
     * 计算TopN的N个最大数字
     *
     * @param element 海量数字之一
     */
    public void countTopN(long element) {
        //如果优先队列里面的数字已经有N个了，就需要判断当前的数字是否大于优先队列里面的最大数字
        if (priorityQueue.size() == topNum) {
            //查看优先队列中最小数字
            long minimum = priorityQueue.peek();
            //如果当前数字比优先队列中最小数字要大
            if (element > minimum) {
                //把优先队列中最小数字出队
                priorityQueue.poll();
                //把当前数字放入优先队列中
                priorityQueue.add(element);
            }
        } else {
            priorityQueue.add(element);
        }
    }

    public long[] getTopN() {
        //创建一个长度为N的数组
        long[] result = new long[topNum];
        //把优先队列中的数字放入数组中
        for (int i = 0; i < result.length; i++) {
            result[i] = priorityQueue.poll();
        }
        //返回TopN的结果数组
        return result;
    }

    public static void main(String[] args) throws Exception {
        // 创建stopwatch并开始计时
        Stopwatch stopwatch = Stopwatch.createStarted();
        //模拟海量大数据
        long[] bigData = new long[10000];
        //随机数生成器
        Random random = new Random();
        //生成百万级的"大文件"上的数据
        for (int i = 0; i < bigData.length; i++) {
            bigData[i] = random.nextInt(10000000);
        }
        //创建TopN工具类对象,统计最大的20个数字
        TopN topN = new TopN(20);
        for (long bigDatum : bigData) {
            topN.countTopN(bigDatum);
        }
        stopwatch.stop();
        System.err.println(stopwatch.elapsed(TimeUnit.SECONDS));
        stopwatch.reset().start();
        Thread.sleep(1500);
        System.out.println(stopwatch);
        System.out.println(stopwatch.elapsed(TimeUnit.SECONDS));

        //统计完毕，获取Top20的最大的20个数字
        long[] result = topN.getTopN();


        //打印结果
        System.err.println("Top20的最大数字是：" + Arrays.toString(result));
        stopwatch.stop();
        System.err.println(stopwatch.elapsed(TimeUnit.SECONDS));
        // 检查isRunning
        System.out.println("-- 检查isRunning --");
        System.out.println(stopwatch.isRunning());

        // 打印
        System.out.println("-- 打印 --");
        System.out.println(stopwatch.toString());

    }
}
