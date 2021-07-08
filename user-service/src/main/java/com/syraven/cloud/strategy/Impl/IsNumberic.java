package com.syraven.cloud.strategy.Impl;

import com.syraven.cloud.strategy.ValidationStrategy;

/**
 * @author SyRAVEN
 * @since 2021-04-08 09:15
 */
public class IsNumberic implements ValidationStrategy {
    @Override
    public boolean execute(String s) {
        return s.matches("\\d+");
    }
}
