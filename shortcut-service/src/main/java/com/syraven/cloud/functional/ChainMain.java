package com.syraven.cloud.functional;

import com.syraven.cloud.handler.ChainHandler;

import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * @ClassName: ChainMain
 * @Description:
 * @Author syrobin
 * @Date 2022-02-18 3:31 PM
 * @Version V1.0
 */
public class ChainMain {

    public static void main(String[] args) {
        UnaryOperator<String> addHeaderHandler = ChainHandler.addHeaderHandler();
        UnaryOperator<String> checkSpellHandler = ChainHandler.checkSpellHandler();
        UnaryOperator<String> addFooterHandler = ChainHandler.addFooterHandler();
        Function<String, String> pipeline = addHeaderHandler
                .andThen(checkSpellHandler)
                .andThen(addFooterHandler);
        String test = pipeline.apply("labda");
        System.out.println(test);

    }
}
