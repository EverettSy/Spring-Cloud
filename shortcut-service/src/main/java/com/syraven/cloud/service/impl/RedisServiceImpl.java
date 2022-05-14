package com.syraven.cloud.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.syraven.cloud.domain.UserRoot;
import com.syraven.cloud.service.RedisService;
import com.syraven.cloud.utlis.KryoUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @ClassName: RedisServiceImpl
 * @Description:
 * @Author syrobin
 * @Date 2021-11-01 3:47 下午
 * @Version V1.0
 */
@Slf4j
@Service
@AllArgsConstructor
public class RedisServiceImpl implements RedisService {

    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> getInfo() {
        Properties info = (Properties) redisTemplate.execute((RedisCallback) RedisServerCommands::info);
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback)
                redisConnection -> redisConnection.info("commandstats"));
        Object dbSize = redisTemplate.execute(RedisServerCommands::dbSize);

        Map<String, Object> result = Maps.newHashMap();
        result.put("info", info);
        result.put("dbSize", dbSize);
        result.put("time", DateUtil.format(new Date(), DatePattern.NORM_TIME_PATTERN));

        List<Map<String, String>> pieList = new ArrayList<>();
        if (Objects.isNull(commandStats)){
            return result ;
        }
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = Maps.newHashMap();
            String property = commandStats.getProperty(key);
            data.put("name", StrUtil.removePrefix(key, "cmdstat_"));
            data.put("value", StrUtil.subBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return result;
    }

    public String UserInfo(){
        UserRoot userRoot = UserRoot.builder()
                .username("张三")
                .age(12)
                .id(12)
                .password("123456")
                .build();

        String userInfoRedis = KryoUtil.writeToString(userRoot);
        redisTemplate.opsForValue().set("user",userInfoRedis);
        return userInfoRedis;



    }
}
