package com.syraven.cloud.spring.context;

import com.syraven.cloud.spring.context.bean.RootContext;
import com.syraven.cloud.spring.context.config.child1.ChildContext1;
import com.syraven.cloud.spring.context.config.child2.ChildContext2;
import com.syraven.cloud.spring.context.config.child3.ChildContext3;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @ClassName: ContextMain
 * @Description:
 * @Author syrobin
 * @Date 2021-12-02 10:01 PM
 * @Version V1.0
 */
public class ContextMain {

    public static void main(String[] args) {
        SpringApplicationBuilder appBuilder =
                new SpringApplicationBuilder()
                        .sources(RootContext.class)
                        //第一个子context用child，剩下的都用sibling
                        .child(ChildContext1.class)
                        .sibling(ChildContext2.class)
                        .sibling(ChildContext3.class);
        ConfigurableApplicationContext applicationContext = appBuilder.run();
    }
}
