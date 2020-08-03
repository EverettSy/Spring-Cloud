package com.syraven.cloud.service.impl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/6/29 15:58
 */
public class SpringTest {

    private static Map<String, Object> cacheMap = new HashMap<String, Object>(2);


    public static void main(String[] args) throws Exception {
        Class[] classes = {A.class,B.class};

        for (Class aClass : classes) {
            getBean(aClass);
        }
    }

    private static <T> T getBean(Class<T> beanClass) throws Exception{
        //本文用类名小写 简单代替bean的命名规则
        String beanName = beanClass.getSimpleName().toLowerCase();

        //如果已经是一个bean，则直接返回
        if (cacheMap.containsKey(beanName)){
            return (T) cacheMap.get(beanName);
        }

        // 将对象本身实例化
        Object object = beanClass.getDeclaredConstructor().newInstance();
        cacheMap.put(beanName,object);

        //把所有字段当成需要注入的bean，创建并注入到当前bean中
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            //获取需要注入字段的class
            Class<?> fieldClass = field.getType();
            String fieldBeanName = fieldClass.getSimpleName().toLowerCase();

            //如果需要注入的bean，已经在缓存Map中，那么把缓存Map中的值注入到该field即可
            // 如果缓存没有 继续创建
            field.set(object,cacheMap.containsKey(fieldBeanName) ? cacheMap.get(fieldBeanName): getBean(fieldClass));
        }

        //属性填充完成，返回
        return (T) object;
    }
}

class A {
    private B b;

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }

}

class B {
    private A a;

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }
}


