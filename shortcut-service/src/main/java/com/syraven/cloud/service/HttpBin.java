package com.syraven.cloud.service;

import feign.RequestLine;

/**
 * @ClassName: HttpBin
 * @Description:
 * @Author syrobin
 * @Date 2021-12-29 5:16 PM
 * @Version V1.0
 */
public interface HttpBin {

    /*@Get(uri = "/get")
    String get();

    @RequestLine("POST /anything")
    Object postBody(Map<String,String> body);*/

    //发到这个链接的所有请求，响应会返回请求中的所有元素
    @RequestLine("GET /anything")
    String anything();
}
