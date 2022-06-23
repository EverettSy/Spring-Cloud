package com.syraven.cloud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * <<功能简述>>
 * 默认情况下添加SpringSecurity依赖的应用每个请求都需要添加CSRF token才能访问，
 * Eureka客户端注册时并不会添加，所以需要配置/eureka/**路径不需要CSRF token。
 * @author Raven
 * @date 2019/11/7 23:35
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().ignoringAntMatchers("/eureka/***").disable(); //关闭csrf
        super.configure(http); //开启认证
    }
}