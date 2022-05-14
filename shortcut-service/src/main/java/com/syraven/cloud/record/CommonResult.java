package com.syraven.cloud.record;

import com.alibaba.fastjson.JSON;
import com.syraven.cloud.common.enums.ResultCodeEnum;
import com.syraven.cloud.common.response.ResultSupport;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @ClassName CommonResult
 * @Description: 非分页对象统一响应消息报文
 * @Author syrobin
 * @Date 2021-08-2021/8/9-1:45 下午
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Schema(name = "消息报文", title = "非分页对象统一响应")
public class CommonResult<T> implements Serializable, ResultSupport {

    private static final long serialVersionUID = 6793350277616007958L;

    @Schema(name = "状态码", required = true)
    private Integer code;

    @Schema(name = "消息内容", required = true)
    private String msg;

    @Schema(name = "业务数据")
    private T data;

    public static <T> CommonResult<T> success() {
        return restResult(HttpStatus.OK.value(), ResultCodeEnum.SUCCESS.getMsg(), null);
    }

    public static <T> CommonResult<T> success(T data) {
        return restResult(HttpStatus.OK.value(), ResultCodeEnum.SUCCESS.getMsg(), data);
    }

    public static <T> CommonResult<T> success(T data, String msg) {
        return restResult(HttpStatus.OK.value(), msg, data);
    }

    public static <T> CommonResult<T> error() {
        return restResult(ResultCodeEnum.ERROR.getCode(), ResultCodeEnum.ERROR.getMsg(), null);
    }

    public static <T> CommonResult<T> error(String msg) {
        return restResult(ResultCodeEnum.ERROR.getCode(), msg, null);
    }

    public static <T> CommonResult<T> error(T data) {
        return restResult(ResultCodeEnum.ERROR.getCode(), ResultCodeEnum.ERROR.getMsg(), data);
    }

    public static <T> CommonResult<T> error(Integer code, String msg) {
        return restResult(code, msg, null);
    }

    public static <T> CommonResult<T> error(Integer code, String msg, T data) {
        return restResult(code, msg, data);
    }

    public static <T> CommonResult<T> error(ResultCodeEnum resultCodeEnum) {
        return restResult(resultCodeEnum.getCode(), resultCodeEnum.getMsg(), null);
    }

    public static <T> CommonResult<T> error(ResultCodeEnum resultCodeEnum, String msg) {
        return restResult(resultCodeEnum.getCode(), msg, null);
    }

    private static <T> CommonResult<T> restResult(int code, String msg, T data) {
        CommonResult<T> apiResult = new CommonResult<>();
        apiResult.setCode(code);
        apiResult.setMsg(msg);
        apiResult.setData(data);
        return apiResult;
    }

    /**
     * 得到JSON格式字符串
     *
     * @return JSON格式字符串
     */
    public String toJsonStr() {
        return JSON.toJSONString(this);
    }
}
