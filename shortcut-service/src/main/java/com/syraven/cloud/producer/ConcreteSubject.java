package com.syraven.cloud.producer;

import com.syraven.cloud.service.Subject;

/**
 * @ClassName: ConcreteSubject
 * @Description: 拉模型的具体主题类
 * @Author syrobin
 * @Date 2021-11-23 2:51 下午
 * @Version V1.0
 */
public class ConcreteSubject extends Subject {

    private String state;

    public String getState() {
        return state;
    }

    public void change(String newState) {
        state = newState;
        System.out.println("主题状态为：" + state);
        //状态发生变更，通知各个观察者
        this.nodifyObservers();

    }
}
