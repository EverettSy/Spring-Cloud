package com.syraven.cloud.consumer;

import com.syraven.cloud.producer.ConcreteSubject;
import com.syraven.cloud.service.Observer;
import com.syraven.cloud.service.Subject;

/**
 * @ClassName: ConcreteObserver
 * @Description: 拉模型的具体观察者类
 * @Author syrobin
 * @Date 2021-11-23 2:55 下午
 * @Version V1.0
 */
public class ConcreteObserver implements Observer {

    //观察者的状态
    private String observerState;

    @Override
    public void update(Subject subject) {
        observerState = ((ConcreteSubject)subject).getState();
        System.out.println("观察者状态为："+ observerState);

    }
}
