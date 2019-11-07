package com.syraven.cloud.controller;

import com.syraven.cloud.domain.CommonResult;
import com.syraven.cloud.domain.User;
import com.syraven.cloud.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2019/11/7 22:14
 */
@RestController
@RequestMapping("/user")
public class UserController {

    public static final  Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public CommonResult create(@RequestBody User user){
        userService.create(user);
        return new CommonResult<>("操作成功",200);
    }

    @GetMapping("/{id}")
    public CommonResult<User> getUser(@PathVariable Integer id){
        User user = userService.getUser(id);
        LOGGER.info("根据id获取用户信息，用户名称为：{}",user.getUsername());
        return new CommonResult<>(user);
    }

    @GetMapping("/getUserByIds")
    public CommonResult<List<User>> getUserByIds(@RequestParam List<Integer> ids) {
        List<User> userList= userService.getUserByIds(ids);
        LOGGER.info("根据ids获取用户信息，用户列表为：{}",userList);
        return new CommonResult<>(userList);
    }

    @GetMapping("/getByUsername")
    public CommonResult<User> getByUsername(@RequestParam String username) {
        User user = userService.getByUsername(username);
        return new CommonResult<>(user);
    }

    @PostMapping("/update")
    public CommonResult update(@RequestBody User user) {
        userService.update(user);
        return new CommonResult("操作成功", 200);
    }

    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Integer id) {
        userService.delete(id);
        return new CommonResult("操作成功", 200);
    }
}