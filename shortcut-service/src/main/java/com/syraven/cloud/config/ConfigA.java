package com.syraven.cloud.config;

import com.syraven.cloud.annotation.EnableService;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: ConfigA
 * @Description:
 * @Author syrobin
 * @Date 2021-11-17 8:21 下午
 * @Version V1.0
 */
//@Import(ServiceImportSelector.class)
@Configuration
//@EnableService(name = "B")
@EnableService(name = "TestServiceC")
public class ConfigA {

    /*@Bean
    @ConditionalOnMissingBean
    public ServiceInterface getServiceA(){
        return new ServiceA();
    }*/
}
