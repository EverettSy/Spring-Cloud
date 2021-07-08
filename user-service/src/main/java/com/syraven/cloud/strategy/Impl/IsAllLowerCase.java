package com.syraven.cloud.strategy.Impl;

import com.syraven.cloud.strategy.ValidationStrategy;

/**
 * @author SyRAVEN
 * @since 2021-04-08 09:00
 */
public class IsAllLowerCase implements ValidationStrategy {
    @Override
    public boolean execute(String s) {
        return s.matches("[a-z]+");
    }
}
