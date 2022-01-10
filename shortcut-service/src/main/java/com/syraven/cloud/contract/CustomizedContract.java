package com.syraven.cloud.contract;

import com.syraven.cloud.annotation.Get;
import feign.Contract;
import feign.MethodMetadata;
import feign.Request;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @ClassName: CustomizedContract
 * @Description: 自定注解 Contract
 * 外部自定义必须继承 BaseContract，因为里面生成的 MethodMetadata 的构造器是 package private 的
 * @Author syrobin
 * @Date 2021-12-29 4:59 PM
 * @Version V1.0
 */
public class CustomizedContract extends Contract.BaseContract {
    @Override
    protected void processAnnotationOnClass(MethodMetadata data, Class<?> clz) {
        //处理类上面的注解，这里没用到

    }

    @Override
    protected void processAnnotationOnMethod(MethodMetadata data, Annotation annotation, Method method) {
        //处理方法上的注解
        Get get = method.getAnnotation(Get.class);
        //如果Get 注解存在，则指定方法 HTTP 请求方式为GET,同时uri 指定为注解uri()的返回
        if (get != null){
            data.template().method(Request.HttpMethod.GET);
            data.template().uri(get.uri());
        }
    }

    @Override
    protected boolean processAnnotationsOnParameter(MethodMetadata data, Annotation[] annotations, int paramIndex) {
        //处理参数上面的注解
        return false;
    }
}
