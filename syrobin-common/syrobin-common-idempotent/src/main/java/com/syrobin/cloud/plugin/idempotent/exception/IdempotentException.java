package com.syrobin.cloud.plugin.idempotent.exception;

/**
 * @ClassName: IdempotentException
 * @Description: 幂等异常处理
 * @Author syrobin
 * @Date 2021-11-27 11:41 上午
 * @Version V1.0
 */
public class IdempotentException extends RuntimeException{

    public IdempotentException(){
        super();
    }

    public IdempotentException(String message){
        super(message);
    }

    public IdempotentException(String message,Throwable cause){
        super(message, cause);
    }

    public IdempotentException(Throwable cause){
        super(cause);
    }

    protected IdempotentException(String message,Throwable cause
            ,boolean enableSuppression,boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
