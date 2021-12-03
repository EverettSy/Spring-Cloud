package com.syraven.cloud.spring.context.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @ClassName: RootContext
 * @Description:
 * @Author syrobin
 * @Date 2021-12-02 9:01 PM
 * @Version V1.0
 */
@Configuration
@PropertySource(value = "classpath:/root.yaml",factory = YamlPropertyLoaderFactory.class)
public class RootContext {

    @Bean
    public RootBean getFatherBean(){
        RootBean rootBean = new RootBean();
        rootBean.setName("root");
        return rootBean;
    }
}
