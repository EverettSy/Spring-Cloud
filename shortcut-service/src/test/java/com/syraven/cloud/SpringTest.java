package com.syraven.cloud;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName: ServiceC
 * @Description:
 * @Author syrobin
 * @Date 2022-02-25 8:04 PM
 * @Version V1.0
 */

//这是JUnit的注解，通过这个注解让SpringJUnit4ClassRunner这个类提供Spring测试上下文。
@RunWith(SpringJUnit4ClassRunner.class)
//这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = ShortCutApplication.class)
//测试数据回滚
@Transactional
public abstract class SpringTest {
}
