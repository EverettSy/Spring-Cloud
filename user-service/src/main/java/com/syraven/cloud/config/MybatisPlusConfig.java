package com.syraven.cloud.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.syraven.cloud.props.TenantProperties;
import lombok.AllArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * com.syraven.cloud.core.mybatis plus配置中心
 *
 * @author SyRAVEN
 * @since 2021-04-01 14:48
 */
@Configuration
@AllArgsConstructor
@EnableTransactionManagement
//引入外部配置文件
//@PropertySource(factory = YamlPropertiesFactoryBean.class,value = "classpath:mate-db.yml")
@MapperScan("com.syraven.cloud.**.mapper.**")
public class MybatisPlusConfig {

    private final TenantProperties tenantProperties;

    private final TenantLineInnerInterceptor tenantLineInnerInterceptor;

    /**
     * 单页分页条数限制（默认无限制，参见插件#handlerLimit 方法）
     */
    public static final Long MAX_LIMIT = 1000L;

    public ISqlInjector sqlInjector(){
        return null;
    }
}
