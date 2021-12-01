package com.syrobin.cloud.plugin.idempotent.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: Idempotent
 * @Description: 幂等注解
 * @Author syrobin
 * @Date 2021-11-26 9:41 上午
 * @Version V1.0
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Idempotent {

    /**
     * 幂等操作的唯一标识，使用spring el表达式 用#来引用方法参数
     * @return Spring-EL expression
     */
    String key() default "";

    /**
     * 有效期 默认：1 有效期要大于程序执行时间，否则请求还是可能会进来
     * @return expireTIme
     */
    int expireTime() default 1;

    /**
     * 时间单位 默认： 秒s
     * @return TimeUnit
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 提示信息，可自定义
     * @return String
     */
    String info() default "重复请求，请稍后重试";

    /**
     * 是否在业务完成后删除key true:删除 false:不删除
     * @return boolean
     */
    boolean delKey() default false;
}
