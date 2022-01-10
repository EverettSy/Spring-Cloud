package com.syraven.cloud.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//仅可用于方法上
@Target(ElementType.METHOD)
//指定注解保持到运行时
@Retention(RetentionPolicy.RUNTIME)
public @interface Get {

    //请求 uri
    String uri();
}
