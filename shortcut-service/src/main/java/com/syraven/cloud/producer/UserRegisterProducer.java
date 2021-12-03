package com.syraven.cloud.producer;

import com.syraven.cloud.event.UserRegisterEvent;
import com.syraven.cloud.service.AbstractProducer;
import org.springframework.stereotype.Component;

/**
 * @ClassName: UserRegisterProducer
 * @Description: 主题对象，提供注册主题方法
 * @Author syrobin
 * @Date 2021-11-23 10:45 上午
 * @Version V1.0
 */
@Component
public class UserRegisterProducer extends AbstractProducer<UserRegisterEvent> {
    @Override
    public void post(UserRegisterEvent event) {
        eventBus.post(event);
    }
}
