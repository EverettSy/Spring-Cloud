package com.syraven.cloud.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * @ClassName: AddHeaderRequestInterceptor
 * @Description:
 * @Author syrobin
 * @Date 2021-12-30 4:51 PM
 * @Version V1.0
 */
public class AddHeaderRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        //添加header
        template.header("test-header","test-value");
    }
}
