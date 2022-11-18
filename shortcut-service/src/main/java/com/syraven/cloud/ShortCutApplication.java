package com.syraven.cloud;

import cn.hippo4j.core.enable.EnableDynamicThreadPool;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.syraven.cloud.utlis.JwtUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * @author YuSong
 */
@EnableDynamicThreadPool
@EnableDiscoveryClient
@SpringBootApplication
//@ComponentScan(basePackages = {"com.syraven.cloud.*"})
@MapperScan("com.syraven.cloud.mapper")
public class ShortCutApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortCutApplication.class, args);
    }

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }
}
