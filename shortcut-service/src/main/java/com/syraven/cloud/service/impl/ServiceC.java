package com.syraven.cloud.service.impl;

import com.syraven.cloud.service.ServiceInterface;

/**
 * @ClassName: ServiceC
 * @Description:
 * @Author syrobin
 * @Date 2021-12-02 8:04 PM
 * @Version V1.0
 */
public class ServiceC implements ServiceInterface {

    private final String name;

    public ServiceC(String name) {
        this.name = name;
    }

    @Override
    public void test() {
        System.out.println(name);
    }
}
