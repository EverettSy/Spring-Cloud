package com.syraven.cloud.selector;

import com.syraven.cloud.annotation.EnableService;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Objects;

/**
 * @ClassName: DeferredImportSelector
 * @Description:
 * @Author syrobin
 * @Date 2021-12-02 7:44 PM
 * @Version V1.0
 */
public class DefferredServiceImportSelector implements DeferredImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(EnableService.class.getName(), true);
        String name = (String) map.get("name");
        if (Objects.equals(name, "B")) {
            return new String[]{"com.syraven.cloud.config.ConfigB"};
        }
        return new String[0];
    }
}
