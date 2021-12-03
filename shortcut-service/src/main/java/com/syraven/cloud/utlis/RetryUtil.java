package com.syraven.cloud.utlis;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @ClassName: RetryUtil
 * @Description: 重试工具类
 * @Author syrobin
 * @Date 2021-11-15 4:17 下午
 * @Version V1.0
 */
@Slf4j
public class RetryUtil {

    /**
     * 方法重试调用（未执行成功才进行重试）
     * 不处理方法调用异常
     *
     * @param invokeMethod          执行方法
     * @param retryCondition        重试条件
     * @param fixedWaitMilliseconds 充实间隔时间（毫秒）
     * @param retryCount            重试次数
     * @param exception             达到最大重试次数，抛出指定异常
     * @param <U>
     * @return 方法返回
     */
    public static <U> U invoke(Supplier<U> invokeMethod,
                               Predicate<U> retryCondition,
                               long fixedWaitMilliseconds,
                               int retryCount,
                               Supplier<? extends RuntimeException> exception) {
        return RetryUtil.invoke(invokeMethod, retryCondition, fixedWaitMilliseconds, retryCount, null);
    }


    /**
     * 方法重试调用（未执行成功才进行重试）
     * 可手动处理异常，设置默认值
     * @param invokeMethod 执行方法
     * @param retryCondition 重试条件
     * @param fixedWaitMilliseconds 重试间隔时间（毫秒）
     * @param retryCount 重试次数
     * @param exceptionally 执行方法发生异常处理，返回默认值，为null 则直接上抛异常
     * @param exception
     * @param <U>
     * @return
     */
    public static <U> U invoke(Supplier<U> invokeMethod,
                               Predicate<U> retryCondition,
                               long fixedWaitMilliseconds,
                               int retryCount,
                               Function<Exception, U> exceptionally,
                               Supplier<? extends RuntimeException> exception){
        if (invokeMethod == null || retryCondition == null || exception == null){
            throw new NullPointerException();
        }
        U result;
        try {
            result = invokeMethod.get();
        } catch (Exception e) {
            log.error("",e);
            if (exceptionally == null){
                throw e;
            }
            return exceptionally.apply(e);
        }
        if (!retryCondition.test(result)){
            return result;
        }
        if (retryCount > 0){
            try {
                TimeUnit.MILLISECONDS.sleep(fixedWaitMilliseconds);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log.error("retryCount: {}",retryCount);
            return invoke(invokeMethod,retryCondition,fixedWaitMilliseconds,--retryCount,exceptionally,exception);
        }
        throw exception.get();
    }


}
