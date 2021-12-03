package com.syraven.cloud.selector;

import com.syraven.cloud.annotation.EnableService;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Objects;

/**
 * @ClassName: ImportSelector
 * @Description:
 * @Author syrobin
 * @Date 2021-11-17 8:31 下午
 * @Version V1.0
 */
public class ServiceAImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //这里的importingClassMetadata针对的是使用@EnableService的非注解类
        //因为`AnnotationMetadata`是`Import`注解所在的类属性，如果所在类是注解类，则延伸至应用这个注解类的非注解类为止
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnableService.class.getName(),true);
        String name = (String) map.get("name");
        if (Objects.equals(name,"B")){
            return new String[]{"com.syraven.cloud.config.ConfigB"};
        }
        return new String[0];
    }
}
