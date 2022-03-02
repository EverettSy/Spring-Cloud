package com.syraven.cloud.handler;

import java.util.function.UnaryOperator;

/**
 * @ClassName: ChainHandler
 * @Description:
 * @Author syrobin
 * @Date 2022-02-18 3:28 PM
 * @Version V1.0
 */
public class ChainHandler {

    public static UnaryOperator<String> addHeaderHandler() {
        return input -> "From Raoul, Mario and Alan: " + input;
    }

    public static UnaryOperator<String> checkSpellHandler() {
        return input -> input.replace("labda", "lambda");
    }

    public static UnaryOperator<String> addFooterHandler() {
        return input -> input + " Kind regards";
    }

    private ChainHandler() {
        throw new IllegalStateException("Utility class");
    }
}
