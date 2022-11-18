package com.syraven.cloud.utlis;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

/**
 * @author syrobin
 * @version v1.0
 * @description: 异常提取工具类
 * @date 2022-10-23 18:48
 */
public class ExceptionUtils {

    public static Throwable extractRealException(Throwable throwable) {
        if (throwable instanceof CompletionException || throwable instanceof ExecutionException) {
            if (throwable.getCause() != null) {
                return throwable.getCause();
            }
        }
        return throwable;
    }

}
