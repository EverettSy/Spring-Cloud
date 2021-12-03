package com.syraven.cloud.spring.context.bean;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;

/**
 * @ClassName: YamlPropertyLoaderFactory
 * @Description: Yaml属性加载器工厂
 * @Author syrobin
 * @Date 2021-12-02 9:24 PM
 * @Version V1.0
 */
public class YamlPropertyLoaderFactory extends DefaultPropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        if (resource == null){
            return super.createPropertySource(name, resource);
        }

        return new YamlPropertySourceLoader().load(resource.getResource().getFilename(), resource.getResource()).get(0);
    }
}
