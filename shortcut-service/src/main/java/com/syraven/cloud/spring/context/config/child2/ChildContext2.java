package com.syraven.cloud.spring.context.config.child2;

import com.syraven.cloud.spring.context.bean.ChildBean;
import com.syraven.cloud.spring.context.bean.RootBean;
import com.syraven.cloud.spring.context.bean.YamlPropertyLoaderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * @ClassName: ChildContext2
 * @Description:
 * @Author syrobin
 * @Date 2021-12-02 9:47 PM
 * @Version V1.0
 */
@SpringBootApplication(scanBasePackages= {"com.syraven.cloud.controller"})
@PropertySource(value = "classpath:/bean-config-2.yaml", factory = YamlPropertyLoaderFactory.class)
public class ChildContext2 {

    @Bean
    public ChildBean getChildBean(@Value("${spring.application.name}") String name,
                                  RootBean fatherBean) {
        ChildBean childBean = new ChildBean();
        childBean.setFatherBean(fatherBean);
        childBean.setName(name);
        return childBean;
    }
}
