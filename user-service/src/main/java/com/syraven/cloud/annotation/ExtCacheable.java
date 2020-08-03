package com.syraven.cloud.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author SyRaven
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtCacheable {

    String key() default "";

    String nextKey() default "";

    long expireTime() default 1L;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;
}
