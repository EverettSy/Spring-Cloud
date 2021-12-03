package com.syraven.cloud.spring.context.config.child1;

import com.syraven.cloud.spring.context.bean.ChildBean;
import com.syraven.cloud.spring.context.bean.RootBean;
import com.syraven.cloud.spring.context.bean.YamlPropertyLoaderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * @ClassName: ChildContext1
 * @Description:
 * @Author syrobin
 * @Date 2021-12-02 9:35 PM
 * @Version V1.0
 */
@SpringBootApplication(scanBasePackages= {"com.syraven.cloud.controller"})
@PropertySource(value = "classpath:/bean-config-1.yaml", factory = YamlPropertyLoaderFactory.class)
public class ChildContext1 {

    @Bean
    public ChildBean getChildBean(@Value("$spring.application.name") String name, RootBean fatherBean){
        ChildBean childBean = new ChildBean();
        childBean.setFatherBean(fatherBean);
        childBean.setName(name);
        return childBean;
    }
}
