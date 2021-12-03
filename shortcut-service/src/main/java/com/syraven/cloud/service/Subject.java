package com.syraven.cloud.service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Subject
 * @Description: 拉模型的抽象主题类
 * @Author syrobin
 * @Date 2021-11-23 2:25 下午
 * @Version V1.0
 */
public abstract class Subject {

    /**
     * 用于保存注册的观察者对象
     */
    private List<Observer> observerList = new ArrayList<>();

    /**
     * 注册观察者对象
     * @param observer 观察者对象
     */
    public void attach(Observer observer){
        observerList.add(observer);
        System.out.println("Attached an observer");
    }

    /**
     * 删除观察者对象
     * @param observer 观察者对象
     */
    public void detach(Observer observer){
        observerList.remove(observer);
    }


    /**
     * 通知所有注册的观察者对象
     */
    public void nodifyObservers(){
        for (Observer observer : observerList) {
            observer.update(this);
        }

    }
}
