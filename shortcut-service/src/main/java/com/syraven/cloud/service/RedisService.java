package com.syraven.cloud.service;

import java.util.Map;

/**
 * @ClassName: RedisService
 * @Description: redis 监控
 * @Author syrobin
 * @Date 2021-11-01 3:45 下午
 * @Version V1.0
 */
public interface RedisService {

    /**
     * 获取内存信息
     * @return
     */
    Map<String,Object> getInfo();
}
