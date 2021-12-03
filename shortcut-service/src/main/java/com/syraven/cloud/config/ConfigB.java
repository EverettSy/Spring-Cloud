package com.syraven.cloud.config;

import com.syraven.cloud.service.ServiceInterface;
import com.syraven.cloud.service.impl.ServiceB;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: ConfigB
 * @Description:
 * @Author syrobin
 * @Date 2021-11-17 8:22 下午
 * @Version V1.0
 */
@Configuration
public class ConfigB {

    @Bean
    @ConditionalOnMissingBean
    public ServiceInterface getServiceB(){
        return new ServiceB();
    }
}
