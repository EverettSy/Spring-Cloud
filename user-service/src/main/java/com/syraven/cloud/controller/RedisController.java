package com.syraven.cloud.controller;

import com.syraven.cloud.annotation.ExtCacheable;
import com.syraven.cloud.domain.UserVo;
import com.syraven.cloud.util.CacheTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/6/23 15:34
 */
@RestController
@RequestMapping("/hello")
@Slf4j
public class RedisController {


    @Value("${productDetailCouponCache.begin}")
    private int cacheTimeBegin = 240;

    @Value("${productDetailCouponCache.end}")
    private int cacheTimeEnd = 361;

    private int expireTime = CacheTimeUtil.generateRandomCacheTime(cacheTimeBegin,cacheTimeEnd);

    /**
     * 不写默认使用带有@Primary的RedisTemplate
     */
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    @Qualifier("secondaryRedisTemplate")
    private RedisTemplate redisTemplate2;

    @ExtCacheable(key = "testRedis",expireTime = 20,timeUnit = TimeUnit.MINUTES)
    @GetMapping("/testRedis")
    public void testRedis(){

        UserVo user = UserVo.builder().name("张三").age(20).address("杭州市").build();
        redisTemplate.opsForValue().set("A",user, 20,TimeUnit.SECONDS);
        user = UserVo.builder().name("四").age(22).address("上海市").build();
        redisTemplate2.opsForValue().set("B",user, 20,TimeUnit.SECONDS);
    }

    @ExtCacheable(key = "Leaders+#p0+#p1+#p2",expireTime = 20,timeUnit = TimeUnit.MINUTES)
    @GetMapping("/testRedis1/{key}/{key1}/{key2}")
    public UserVo testRedis1(@PathVariable String key, @PathVariable String key1, @PathVariable String key2){

        int age = CacheTimeUtil.generateRandomCacheTime(240,360);
        UserVo user = UserVo.builder().name("张三").age(age).address("杭州市").build();
        /*redisTemplate.opsForValue().set("A",user, 20,TimeUnit.SECONDS);
        user = UserVo.builder().name("四").age(22).address("上海市").build();
        redisTemplate2.opsForValue().set("B",user, 20,TimeUnit.SECONDS);*/
        key = "ley1";
        key1 = "ley1";
        key2 = "ley1";
        return user;
    }
}