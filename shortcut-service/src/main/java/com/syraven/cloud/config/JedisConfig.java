package com.syraven.cloud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.commands.JedisCommands;

import javax.annotation.PreDestroy;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-06-25 18:13
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class JedisConfig {
    private JedisPool pool;

    @Bean
    public JedisCommands jedisCommands(RedisProperties properties) {
        //单机模式
        log.info("redis connect {}", properties.getHost());
        pool = JedisFactory.createJedisPool(properties);
        return JedisFactory.createJedis(pool);
    }

    @PreDestroy
    public void destroy() {
        if (pool != null) {
            log.info("close redis pool");
            pool.close();
        }
    }
}
