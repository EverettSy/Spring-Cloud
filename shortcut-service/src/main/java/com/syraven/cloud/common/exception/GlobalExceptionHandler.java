package com.syraven.cloud.common.exception;

import com.syraven.cloud.common.enums.ResultCodeEnum;
import com.syraven.cloud.record.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName GeneralExceptionHandler
 * @Description: 全局异常处理器
 * @Author syrobin
 * @Date 2021-08-2021/8/9-11:05 上午
 * @Version V1.0
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * form data方式调用接口参数校验失败抛出的异常
     *
     * @param ex
     * @return CommonResult<?>
     */
    @ExceptionHandler(BindException.class)
    public CommonResult<?> bindException(BindException ex) {
        log.error(String.format("程序异常======>: BindException: %s", ex.getMessage()), ex);
        log.warn("BindException:", ex);

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return CommonResult.error(ResultCodeEnum.PARAM_EX.getCode(), ResultCodeEnum.PARAM_EX.getMsg(), collect);

    }

    /**
     * json 请求体调用接口校验失败抛出的异常
     *
     * @param ex
     * @return CommonResult<?>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> collect = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return CommonResult.error(ResultCodeEnum.PARAM_EX.getCode(), ResultCodeEnum.PARAM_EX.getMsg(), collect);
    }

    /**
     * 单个参数校验失败抛出的异常
     * @param ex
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public CommonResult<?> constraintViolationExceptionHandler(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        List<String> collect = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return CommonResult.error(ResultCodeEnum.PARAM_EX.getCode(), ResultCodeEnum.PARAM_EX.getMsg(), collect);
    }


}
