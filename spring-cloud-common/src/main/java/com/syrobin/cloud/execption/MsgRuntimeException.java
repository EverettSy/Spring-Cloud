package com.syrobin.cloud.execption;

import java.io.Serializable;

/**
 * @author syrobin
 * @version v1.0
 * @description: 带有提示信息的运行时异常
 * @date 2022-04-21 17:45
 */
public class MsgRuntimeException  extends RuntimeException implements Serializable {

    private static final long serialVersionUID = -816078288971705898L;

    public MsgRuntimeException(String message) {
        super(message);
    }

    public MsgRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
