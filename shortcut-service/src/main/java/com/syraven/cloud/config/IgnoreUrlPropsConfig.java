package com.syraven.cloud.config;

import lombok.Data;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * description:  放行api
 *
 * @author zhouxinlei
 * @date 2021-10-14 11:08:10
 */
@Data
@RefreshScope
@Component
public class IgnoreUrlPropsConfig {

    /**
     * 认证中心默认忽略验证地址
     */
    private static final String[] SECURITY_ENDPOINTS = {
            "/auth/**",
            "/oauth/**",
            "/actuator/**",
            "/v2/api-docs/**",
            "/swagger/api-docs",
            "/swagger-ui.html",
            "/doc.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/druid/**",
            "/error/**",
            "/assets/**",
            "/auth/logout",
            "/auth/code",
            "/auth/sms-code",
            "/short/**",
            "/app/gonggao/chat/**",
            "/endpoint-websocket/**",
            "/topic/game_chat/**",
            "/gonggao/**"

    };

    private List<String> ignoreUrls = new ArrayList<>();

    /**
     * 首次加载合并ENDPOINTS
     */
    @PostConstruct
    public void initIgnoreSecurity() {
        Collections.addAll(ignoreUrls, SECURITY_ENDPOINTS);
    }
}
