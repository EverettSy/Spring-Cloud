package com.syraven.cloud.controller;

import com.syraven.cloud.record.CommonResult;
import com.syraven.cloud.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/short/redis")
@Api(tags = "Redis")
public class RedisController {

    private final RedisService redisService;

    @ApiOperation(value = "获取Redis的运行信息",httpMethod = "GET")
    @GetMapping("/info")
    public CommonResult memory(){
        return CommonResult.success(redisService.getInfo());
    }

}
