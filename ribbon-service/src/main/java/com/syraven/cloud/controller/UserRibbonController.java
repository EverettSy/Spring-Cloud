package com.syraven.cloud.controller;

import com.syraven.cloud.domain.CommonResult;
import com.syraven.cloud.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2019/11/17 22:49
 */
@RestController
@RequestMapping("/user")
public class UserRibbonController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service-url.user-service}")
    private String userServiceUrl;

    @GetMapping("/{id}")
    public CommonResult getUser(@PathVariable Integer id) {
        return restTemplate.getForObject(userServiceUrl + "/user/{id}", CommonResult.class, id);
    }

    @GetMapping("/getByUsername")
    public CommonResult getByUsername(@RequestParam String username) {
        return restTemplate.getForObject(userServiceUrl + "/user/getByUsername?username={username}", CommonResult.class, username);
    }

    @GetMapping("/getEntityByUsername")
    public CommonResult getEntityByUsername(@RequestParam String username) {
        ResponseEntity<CommonResult> entity = restTemplate.getForEntity(userServiceUrl + "/user/getByUsername?username={username}", CommonResult.class, username);
        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        } else {
            return new CommonResult("操作失败", 500);
        }
    }

    @PostMapping("/create")
    public CommonResult create(@RequestBody User user){
        return restTemplate.postForObject(userServiceUrl + "/user/create", user, CommonResult.class);
    }

    @PostMapping("/update")
    public CommonResult update(@RequestBody User user){
        return restTemplate.postForObject(userServiceUrl + "/user/update", user, CommonResult.class);
    }

    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Integer id){
        return restTemplate.postForObject(userServiceUrl + "/delete/{id}", null, CommonResult.class,id);
    }
}