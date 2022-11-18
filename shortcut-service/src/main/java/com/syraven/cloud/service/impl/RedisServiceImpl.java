package com.syraven.cloud.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.syraven.cloud.domain.UserRoot;
import com.syraven.cloud.service.RedisService;
import com.syraven.cloud.utlis.KryoUtil;
import com.syraven.cloud.utlis.LockUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

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

    private LockUtils lockUtils;

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


    @SneakyThrows
    @Override
    public void batchSetMassStrings() {
        Map<String,String> map = Maps.newHashMap();
        map.put("abc","twelve");
        map.put("def","twelve2");
        lockUtils.setMassStrings(map,10, TimeUnit.MINUTES);
       log.info("批量插入成功:{}",lockUtils.getStr("abc")+" "+lockUtils.getStr("def"));

        RBatch batch = lockUtils.createBatch();
        // 模拟购物车场景，真实场景中请替换店铺ID shopId 和商品ID commodityId
        String field = "shopId:commodityId";
        // 把即将执⾏的命令放进 RBatch
        RMapAsync rMapAsync = batch.getMap("custom:cart" + 32L);
        // 更新value，并返回上一次的值
        String commodityNum = "mapValue"+ (int) (Math.random() * 9 + 100);
        log.info("当前商品数量commodityNum是：{}", commodityNum);
        rMapAsync.putAsync(field, commodityNum);
        rMapAsync.putAsync("test2", "mapValue3");
        rMapAsync.putAsync("test2", "mapValue5");
        rMapAsync.putAsync("test："+ (int) (Math.random() * 900 + 100), String.valueOf((int)(Math.random()*900 + 100)));

        RAtomicLongAsync counter = batch.getAtomicLong("custom:cart:count");

        RFuture<Long> num = counter.incrementAndGetAsync();

        // 执⾏ RBatch 中的所有命令
        BatchResult batchResult = batch.execute();
        // 获取执⾏结果
        List<Object> results = batchResult.getResponses();
        log.info("批量执行结果：{}", results);
        log.info("计数器当前值：{}", num.get());
        lockUtils.deleteBatch("abc","def");
        log.info("批量删除:{}",lockUtils.getStr("abc")+" "+lockUtils.getStr("def"));


    }
}
