package com.syraven.cloud.strategy;

/**
 * 校验策略接口
 * @author SyRAVEN
 * @since 2021-04-08 08:53
 */
public interface ValidationStrategy {
    boolean execute(String s);
}
