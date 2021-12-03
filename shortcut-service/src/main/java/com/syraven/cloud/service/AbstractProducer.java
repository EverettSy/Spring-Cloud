package com.syraven.cloud.service;

import com.google.common.eventbus.AsyncEventBus;

import java.util.concurrent.Executors;

/**
 * @ClassName: AbstractProducer
 * @Description: 抽象主题
 * @Author syrobin
 * @Date 2021-11-22 11:32 上午
 * @Version V1.0
 */
public abstract class AbstractProducer<T> {

    public static final AsyncEventBus eventBus = new AsyncEventBus("_event_async_",
            Executors.newFixedThreadPool(4));

    public void registerAsyncEvent(EventConsumer consumer){
        eventBus.register(consumer);
    }

    public abstract void post(T event);

}
