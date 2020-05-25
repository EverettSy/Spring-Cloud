package com.syraven.cloud.controller;

import com.syraven.cloud.domain.CommonResult;
import com.syraven.cloud.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/1/19 13:55
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserHystrixController {

    @Autowired
    private UserService userService;

    @GetMapping("/testFallBack/{id}")
    public CommonResult testFallBack(@PathVariable Integer id){
        return userService.getUser(id);
    }

    @GetMapping("/testCommand/{id}")
    public CommonResult testCommand(@PathVariable Integer id){
        return userService.getUserCommand(id);
    }

    @GetMapping("/testException/{id}")
    @ApiOperation("使用ignoreExceptions忽略某些异常降级")
    public CommonResult testException(@PathVariable Integer id){
        return userService.getUserException(id);
    }
}