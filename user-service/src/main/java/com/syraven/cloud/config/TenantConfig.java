package com.syraven.cloud.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;

/**
 * 多租户配置中心
 *
 * @author SyRAVEN
 * @since 2021-04-01 14:45
 */
@Configuration
@AllArgsConstructor
@AutoConfigureBefore
public class TenantConfig {
}
