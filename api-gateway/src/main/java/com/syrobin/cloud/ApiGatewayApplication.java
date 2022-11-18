package com.syrobin.cloud;

import com.syrobin.cloud.config.VersionLoadBalancerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author syrobin
 * @version v1.0
 * @description: gateway 网关启动类
 * @date 2022-03-22 14:43
 */
@EnableEurekaClient
@LoadBalancerClient(value = "shortcut-service",configuration = VersionLoadBalancerConfiguration.class)
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
