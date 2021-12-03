package com.syraven.cloud.service;

/**
 * @ClassName: EventConsumer
 * @Description: 抽象观察者
 * @Author syrobin
 * @Date 2021-11-23 10:36 上午
 * @Version V1.0
 */
public interface EventConsumer<T> {

    void consume(T event);
}
