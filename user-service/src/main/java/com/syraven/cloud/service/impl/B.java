package com.syraven.cloud.service.impl;

import org.springframework.stereotype.Component;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/6/29 14:20
 */
@Component
public class B {

    private A a;

    public void setA(A a) {
        this.a = a;
    }

    public B(C c){

    }
}