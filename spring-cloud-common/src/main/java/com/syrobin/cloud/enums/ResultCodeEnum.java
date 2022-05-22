package com.syrobin.cloud.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author syrobin
 * @version v1.0
 * @description: 通用响应码
 * @date 2022-04-22 14:30
 */
@Getter
@AllArgsConstructor
public enum ResultCodeEnum {

    SUCCESS(200, "成功"),
    FAIL(400, "失败"),
    UNKNOWN_ERROR(500, "未知错误"),
    SERVER_ERROR(500, "服务器内部错误"),
    MISSING_PARAMETER(400, "缺少必要参数"),
    PARAMETER_ERROR(400, "参数错误"),
    PERMISSION_DENIED(403, "权限不足"),
    RESOURCE_NOT_FOUND(404, "资源不存在"),
    RESOURCE_EXIST(409, "资源已存在"),
    RESOURCE_OCCUPIED(409, "资源被占用"),
    RESOURCE_LOCKED(409, "资源被锁定"),

    VALIDATE_FAILED(1100, "校验失败"),
    VALIDATE_EMPTY(1101, "参数为空"),
    VALIDATE_INVALID(1102, "参数无效"),
    VALIDATE_REPEAT(1103, "重复"),
    VALIDATE_NOT_EXIST(1104, "不存在"),

    LOGIN_FAILED(1201, "登录失败（用户名或密码错误）"),
    UNAUTHORIZED(1202, "暂未登录或token已经过期"),
    FORBIDDEN(1203, "没有相关权限"),
    ;


    private final Integer code;
    private final String message;

}
