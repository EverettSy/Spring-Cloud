package com.syraven.cloud;

import com.syraven.cloud.consumer.ConcreteObserver;
import com.syraven.cloud.dto.UserDto;
import com.syraven.cloud.event.UserRegisterEvent;
import com.syraven.cloud.producer.ConcreteSubject;
import com.syraven.cloud.producer.UserRegisterProducer;
import com.syraven.cloud.service.Observer;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @ClassName: UserRegisterProducerTest
 * @Description: 观察者模式测试
 * @Author syrobin
 * @Date 2021-11-23 11:02 上午
 * @Version V1.0
 */
public class UserRootRegisterProducerTest extends SpringTest {

    @Resource
    private UserRegisterProducer userRegisterProducer;

    @DisplayName("观察者-推模型")
    @Test
    public void test(){
        UserRegisterEvent event = new UserRegisterEvent();
        event.setUserDto(new UserDto(1l,"张三", LocalDateTime.now()));
        userRegisterProducer.post(event);
    }


    @DisplayName("观察者-拉模型")
    @Test
    @Disabled
    public void testsubject(){
        //创建主题对象
        ConcreteSubject subject = new ConcreteSubject();
        //创建观察者对象
        Observer observer = new ConcreteObserver();
        //将观察者对象登记到主题对象上
        subject.attach(observer);
        //改变主题对象的状态
        subject.change("new state");
    }
}
