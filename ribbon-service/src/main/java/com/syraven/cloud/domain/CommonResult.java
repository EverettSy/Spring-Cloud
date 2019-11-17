package com.syraven.cloud.domain;

import lombok.Data;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2019/11/7 22:18
 */
@Data
public class CommonResult<T> {

    private T data;
    private String message;
    private Integer code;

    public CommonResult() {
    }

    public CommonResult(T data, String message, Integer code) {
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public CommonResult(String message, Integer code) {
       this(null,message,code);
    }

    public CommonResult(T data) {
       this(data,"操作成功",200);
    }


}