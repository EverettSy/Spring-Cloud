package com.syraven.cloud.service.impl;

import com.syraven.cloud.service.ServiceInterface;
import org.springframework.core.annotation.Order;

/**
 * @ClassName: ServiceA
 * @Description:
 * @Author syrobin
 * @Date 2021-11-17 8:19 下午
 * @Version V1.0
 */
@Order(-12)
public class ServiceB implements ServiceInterface {
    @Override
    public void test() {
        System.out.println("ServiceB");
    }
}
