package com.syraven.cloud.consumer;

import com.google.common.eventbus.Subscribe;
import com.syraven.cloud.event.UserRegisterEvent;
import com.syraven.cloud.producer.UserRegisterProducer;
import com.syraven.cloud.service.EventConsumer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @ClassName: UserRegisterSyncCacheConsumer
 * @Description: 观察者对象，监听主题事件(用户注册同步缓存)
 * @Author syrobin
 * @Date 2021-11-23 10:56 上午
 * @Version V1.0
 */
@Component
public class UserRegisterSyncCacheConsumer implements EventConsumer<UserRegisterEvent>, InitializingBean {

    @Resource
    private UserRegisterProducer userRegisterProducer;


    @Subscribe //监听事件
    @Override
    public void consume(UserRegisterEvent event) {
        System.out.println("接收到用户注册事件，开始同步 Cache");
        System.out.println(event.getUserDto());
        System.out.println("接收到用户注册事件，同步 Cache 完毕");
    }

    @Override
    public void afterPropertiesSet() {
        userRegisterProducer.registerAsyncEvent(this);
    }
}
