package com.syraven.cloud;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.syraven.cloud.utlis.JwtUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author YuSong
 */
@EnableKafka
@EnableOpenApi
@EnableEurekaClient
@SpringBootApplication
//@ComponentScan(basePackages = {"com.syraven.cloud.*"})
@MapperScan("com.syraven.cloud.*.mapper")
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
