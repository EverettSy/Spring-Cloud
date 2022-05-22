package com.syrobin.cloud.execption;

import com.syrobin.cloud.enums.ResultCodeEnum;
import com.syrobin.cloud.model.result.R;

import java.io.Serializable;

/**
 * @author syrobin
 * @version v1.0
 * @description: 响应结果 - 异常（带有响应码及提示信息）
 * @date 2022-04-22 14:59
 */
public class RespResultException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 4097787455795205493L;

    /**
     * 响应结果
     */
    private R respResult;

    public RespResultException(ResultCodeEnum respCode, String msg) {
        this(respCode, msg, null);
    }

    public RespResultException(ResultCodeEnum respCode, String msg, Throwable throwable) {
        this(new R(respCode.getCode(), msg,null), throwable);
    }

    public RespResultException(R respResult) {
        this(respResult, null);
    }

    public RespResultException(R respResult, Throwable throwable) {
        super(throwable);
        this.respResult = respResult;
    }


    public R getRespResult() {
        return respResult;
    }
}
