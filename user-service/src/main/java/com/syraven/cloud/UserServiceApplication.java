package com.syraven.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author YuSong
 */
@EnableOpenApi
@EnableEurekaClient
@SpringBootApplication
//@ComponentScan(basePackages = {"com.syraven.cloud.*"})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
