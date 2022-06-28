/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.syraven.cloud.auth.config;

import com.syraven.cloud.config.IgnoreUrlPropsConfig;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 服务安全相关配置
 *
 * @author lengleng
 * @date 2022/1/12
 */
@Setter(onMethod_ = @Autowired)
//@EnableWebSecurity(debug = false)
@Configuration
public class WebSecurityConfiguration {

	private IgnoreUrlPropsConfig ignoreUrlsConfig;


    /**
     * 暴露静态资源
     * <p>
     * https://github.com/spring-projects/spring-security/issues/10938
     *
     * @param http
     * @return
     * @throws Exception
     */
	@Bean
	@Order(0)
	SecurityFilterChain resources(HttpSecurity http) throws Exception {
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
				.authorizeRequests();
		//不需要保护的资源路径允许访问
		for (String url : ignoreUrlsConfig.getUrls()) {
			registry.antMatchers(url).permitAll();
		}
		//允许跨域请求的OPTIONS请求
		registry.antMatchers(HttpMethod.OPTIONS)
				.permitAll();
		http.csrf()// 由于使用的是JWT，我们这里不需要csrf
				.disable()
				.sessionManagement()// 基于token，所以不需要session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.anyRequest()// 除上面外的所有请求全部需要鉴权认证
				.authenticated()
				.and()
				.headers().cacheControl(); // 禁用缓存

		return http.build();
	}


    private static final String[] SECURITY_ENDPOINTS = {
            "/auth/**",
            "/oauth/**",
            "/webjars/**",
            "/druid/**",
            "/error",
            "/assets/**",
            "/app/gonggao/chat/**",
            "/endpoint-websocket/**",
            "/topic/game_chat/**",
            "/gonggao/**",
            "/actuator/**",
            "/v3/api-docs/**",
            "/swagger/api-docs",
            "/swagger-ui/**",
            "/doc.html",
            "/swagger-resources/**",
            "/css/**",
            "/info/**",
            "/short/**",
            "/static/**",
            "/url/**"

    };

}
