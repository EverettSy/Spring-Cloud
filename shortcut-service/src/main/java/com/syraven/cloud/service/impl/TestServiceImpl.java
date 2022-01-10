package com.syraven.cloud.service.impl;

import com.syraven.cloud.service.TestService;

/**
 * @ClassName: TestServiceImpl
 * @Description:
 * @Author syrobin
 * @Date 2022-01-10 11:20 AM
 * @Version V1.0
 */
public class TestServiceImpl implements TestService {
    @Override
    public void test() {
        System.out.println("TestServiceImpl#test is called");
    }
}
