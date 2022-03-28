package com.syraven.cloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config
                = http.requestMatchers().anyRequest()
                .and()
                .formLogin()
                .and()
                .authorizeRequests();
        //忽略验证地址
        http.authorizeRequests(authorizeRequests -> ignoreUrlPropsConfig.getIgnoreUrls().forEach(url ->
                        //任何请求都需要身份认证
                        authorizeRequests.antMatchers(url).permitAll())
                )
                .formLogin(Customizer.withDefaults())
                //csrf跨站请求
                .csrf().disable();
        return http.build();
    }
}
