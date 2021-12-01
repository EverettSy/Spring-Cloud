package com.syrobin.cloud.plugin.idempotent.controller;

import com.syrobin.cloud.plugin.idempotent.annotation.Idempotent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/get")
    @Idempotent(key = "#key",expireTime = 3, info = "请勿重复查询")
    public String get(String key) throws Exception{
        Thread.sleep(200);
        return "success";
    }

    @GetMapping("/noKey")
    @Idempotent(expireTime = 3, info = "请勿重复查询")
    public String noKey() throws Exception {
        Thread.sleep(2000L);
        return "success";
    }

}
