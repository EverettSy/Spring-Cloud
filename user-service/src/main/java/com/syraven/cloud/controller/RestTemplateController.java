package com.syraven.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestTemplate Controller
 *
 * @author SyRAVEN
 * @since 2021-04-29 16:48
 */
@Slf4j
@RestController
@RequestMapping("/rest")
public class RestTemplateController {

    @RequestMapping("getHello")
    public String getHello(){
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return null ;
    }
}
