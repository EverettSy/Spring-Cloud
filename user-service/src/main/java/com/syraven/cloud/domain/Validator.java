package com.syraven.cloud.domain;

import com.syraven.cloud.strategy.ValidationStrategy;
import lombok.AllArgsConstructor;

/**
 * @author SyRAVEN
 * @since 2021-04-08 08:48
 */
@AllArgsConstructor
public class Validator {

    private final ValidationStrategy strategy;

    public boolean validate(String s){
        return strategy.execute(s);
    }
}
