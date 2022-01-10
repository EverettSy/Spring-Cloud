package com.syraven.cloud.handler;

import com.syraven.cloud.service.TestService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @ClassName: SimplePrintMethodInvocationHandler
 * @Description:
 * @Author syrobin
 * @Date 2022-01-10 11:25 AM
 * @Version V1.0
 */
public class SimplePrintMethodInvocationHandler implements InvocationHandler {
    private final TestService testService;
    public SimplePrintMethodInvocationHandler(TestService testService){
        this.testService = testService;
    }

    @Override
    public Object invoke(
            //代理对象
            Object proxy,
            //调用的方法
            Method method,
            //使用的参数
            Object[] args) throws Throwable {
        System.out.println("Invoked method: "+ method.getName());
        //进行实际的调用
        return method.invoke(testService,args);
    }
}
