package com.syraven.cloud.proxy;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/10/21 16:53
 */
public class LiuDeHua implements Star {

    @Override
    public String sing(String name) {
        System.out.println("给我一杯忘情水");

        return "唱完";

    }

    @Override
    public String dance(String name) {
        System.out.println("开心的马骝");

        return "跳完";
    }

    public void sings() {
        Star ldh = new LiuDeHua();
        StarProxy starProxy = new StarProxy();
        starProxy.setTarget(ldh);
        Object obj = starProxy.creatPrObject();
        Star star = (Star) obj;
        System.out.println(star);
    }

    public static void main(String[] args) {
        LiuDeHua liuDeHua = new LiuDeHua();
        liuDeHua.sings();

        int times = 1000000;
        Star ldh = new LiuDeHua();
        StarProxy starProxy = new StarProxy();
        starProxy.setTarget(ldh);

        long time1 = System.currentTimeMillis();
        Star star = (Star) starProxy.creatPrObject();
        long time2 = System.currentTimeMillis();
        System.out.println("jdk创建时间："+(time2 - time1));
    }
}