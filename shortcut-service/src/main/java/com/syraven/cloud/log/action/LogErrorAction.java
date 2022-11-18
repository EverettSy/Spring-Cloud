package com.syraven.cloud.log.action;

import lombok.extern.slf4j.Slf4j;

import java.util.function.BiConsumer;

/**
 * @author syrobin
 * @version v1.0
 * @description: 日志处理实现类
 * 发生异常时，根据是否为业务异常打印日志。
 * 跟CompletableFuture.whenComplete配合使用，不改变completableFuture的结果（正常OR异常）
 * @date 2022-10-23 19:03
 */
@Slf4j
public class LogErrorAction<R> extends AbstractLogAction<R> implements BiConsumer<R, Throwable> {

    public LogErrorAction(String methodName,Object... args) {
        super(methodName, args);
    }

    @Override
    public R apply(R result, Throwable throwable) {
        return null;
    }

    @Override
    public void accept(R result, Throwable throwable) {
       logResult(result,throwable);
    }
}
