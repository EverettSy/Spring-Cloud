package com.syraven.cloud.controller;

import com.syraven.cloud.service.RedisService;
import com.syraven.cloud.utlis.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: RedisController
 * @Description: Redis
 * @Author syrobin
 * @Date 2021-09-07 3:39 下午
 * @Version V1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/redis")
public class RedisController {

    private  final RedisService redisService;

    @GetMapping("/info")
    public CommonResult memory(){
        return CommonResult.success(redisService.getInfo());
    }

}
