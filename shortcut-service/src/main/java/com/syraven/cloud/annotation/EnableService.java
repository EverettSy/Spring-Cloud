package com.syraven.cloud.annotation;


import com.syraven.cloud.registrar.ServiceImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
//@Import(ServiceImportSelector.class)
//@Import(DefferredServiceImportSelector.class)
@Import(ServiceImportBeanDefinitionRegistrar.class)
public @interface EnableService {

    String name();
}
