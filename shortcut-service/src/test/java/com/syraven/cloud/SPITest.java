package com.syraven.cloud;

import com.syraven.cloud.config.ConfigA;
import com.syraven.cloud.service.ServiceInterface;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName: SPITest
 * @Description:
 * @Author syrobin
 * @Date 2021-11-16 5:33 下午
 * @Version V1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SPITest {

    public static void main(String[] args) {

        //SPI 获取bean
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ConfigA.class);
        ServiceInterface bean = ctx.getBean(ServiceInterface.class);
        bean.test();




    }


}
