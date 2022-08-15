package com.syraven.cloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-08-15 11:05
 */
@Data
@Component
@ConfigurationProperties("spring.redis")
public class RedisProperties {

    private String host;
    private String port;
    private String password;
    private int pingConnectionInterval;
}
