package com.syraven.cloud.service.impl;

import com.syraven.cloud.service.ServiceInterface;

/**
 * @ClassName: ServiceA
 * @Description:
 * @Author syrobin
 * @Date 2021-11-17 8:19 下午
 * @Version V1.0
 */
public class ServiceA implements ServiceInterface {
    @Override
    public void test() {
        System.out.println("ServiceA");
    }
}
