package com.syraven.cloud.service;

/**
 * @ClassName: Observer
 * @Description: 拉模型的抽象观察者类
 *  主题对象在通知观察者的时候，只传递少量信息。如果观察者需要更具体的信息，由观察者主动到主题对象中获取，相当于是观察者从主题对象中拉数据。
 *  一般这种模型的实现中，会把主题对象自身通过update()方法传递给观察者，这样在观察者需要获取数据的时候，就可以通过这个引用来获取了。
 * @Author syrobin
 * @Date 2021-11-23 2:27 下午
 * @Version V1.0
 */
public interface Observer {

    /**
     * 更新接口
     * @param subject 传入主题对象，方便获取相应的主题对象的状态
     */
    void update(Subject subject);
}
