package com.syraven.cloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @ClassName: DefaultSecurityConfig
 * @Description: security 安全认证
 * @Author syrobin
 * @Date 2021-08-30 4:04 下午
 * @Version V1.0
 */
@EnableWebSecurity
public class DefaultSecurityConfig {

    @Autowired
    private IgnoreUrlPropsConfig ignoreUrlPropsConfig;

    /**
     * 创建默认用户
     *
     * @return
     */
    @Bean
    public UserDetailsService users() {
        UserDetails user = User.builder()
                .username("syrobin")
                .password("{noop}123456")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * 定义 spring security 拦击链规则
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .mvcMatchers("/auth/*").authenticated()
                )
                .csrf().disable()
                .formLogin(withDefaults());
        return http.build();

    }

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

}
