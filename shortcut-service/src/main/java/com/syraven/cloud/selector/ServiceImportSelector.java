package com.syraven.cloud.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @ClassName: ImportSelector
 * @Description:
 * @Author syrobin
 * @Date 2021-11-17 8:31 下午
 * @Version V1.0
 */
public class ServiceImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        //可以是@Configuration注解修饰的类，也可以是具体的Bean类的全限定名称
        return new String[]{"com.syraven.cloud.config.ConfigB"};
    }
}
