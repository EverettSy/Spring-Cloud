package com.syraven.cloud.codec;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;

import java.util.Date;

/**
 * @ClassName: TestErrorDecoder
 * @Description:
 * @Author syrobin
 * @Date 2021-12-30 8:35 PM
 * @Version V1.0
 */
public class TestErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        //获取错误码对应的 FeignException
        FeignException exception = FeignException.errorStatus(methodKey, response);
        //封装为 RetryableException
        return new RetryableException(
                response.status(),
                exception.getMessage(),
                response.request().httpMethod(),
                exception,
                new Date(),
                response.request()
        );
    }
}
