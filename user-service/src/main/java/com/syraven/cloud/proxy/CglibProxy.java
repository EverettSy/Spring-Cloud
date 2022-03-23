package com.syraven.cloud.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/10/23 15:17
 */
public class CglibProxy implements MethodInterceptor {

    public Object createPObject(Class < ? > clazz){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        System.out.println("收钱");
        return methodProxy.invokeSuper(o,objects);
    }
}