package com.syraven.cloud.service;

import feign.RequestLine;

/**
 * @ClassName: TestHttpBin
 * @Description:
 * @Author syrobin
 * @Date 2021-12-30 8:34 PM
 * @Version V1.0
 */
public interface TestHttpBin {

    //请求一定会返回 500
    @RequestLine("GET /status/500")
    Object get();
}
