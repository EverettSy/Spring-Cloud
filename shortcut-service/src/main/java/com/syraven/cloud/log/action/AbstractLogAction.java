package com.syraven.cloud.log.action;

import com.syraven.cloud.common.exception.ProjectException;
import com.syraven.cloud.common.exception.ShortCutException;
import com.syraven.cloud.utlis.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author syrobin
 * @version v1.0
 * @description: 打印日志工具类
 * @date 2022-10-23 18:51
 */
@Slf4j
public abstract class AbstractLogAction<R>{

    protected final String methodName;
    protected final Object[] args;

    protected AbstractLogAction(String methodName, Object... args) {
        this.methodName = methodName;
        this.args = args;
    }

    protected void logResult(R result,Throwable throwable) {
        if (throwable != null) {
            boolean isBusinessError = throwable instanceof ShortCutException || (throwable.getCause() != null
                    && throwable.getCause() instanceof ShortCutException);
            if (isBusinessError) {
                logBusinessError(throwable);
            } else if (throwable instanceof ProjectException || (throwable.getCause() != null
                    && throwable.getCause() instanceof ProjectException)) {
                log.error("{} project exception, param:{} , error:{}", methodName, args, throwable);

            } else {
                log.error("{} unknown error, param:{} , error:{}", methodName, args, throwable);

            }
        }else {
            if (isLogResult()){
                log.info("{} param:{} , result:{}", methodName, args, result);
            }else {
                log.info("{} param:{}", methodName, args);
            }
        }
    }


    private void logBusinessError(Throwable throwable) {
        log.error("{} business error, param:{} , error:{}", methodName, args, throwable.toString(), ExceptionUtils.extractRealException(throwable));

    }

    private boolean isLogResult() {
        //这里是动态配置开关，用于动态控制日志打印，开源动态配置中心可以使用nacos、apollo等，如果项目没有使用配置中心则可以删除
        return (methodName + "_isLogResult").equals("true");
    }

    public abstract R apply(R result, Throwable throwable);
}
