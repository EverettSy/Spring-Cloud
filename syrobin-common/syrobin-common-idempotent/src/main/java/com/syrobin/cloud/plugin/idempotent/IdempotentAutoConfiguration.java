package com.syrobin.cloud.plugin.idempotent;

import com.syrobin.cloud.plugin.idempotent.aspect.IdempotentAspect;
import com.syrobin.cloud.plugin.idempotent.expression.ExpressionResolver;
import com.syrobin.cloud.plugin.idempotent.expression.KeyResolver;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: IdempotentAutoConfiguration
 * @Description: 幂等插件初始化
 * @Author syrobin
 * @Date 2021-11-27 12:18 下午
 * @Version V1.0
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class IdempotentAutoConfiguration {

    /**
     * 切面 拦截处理所有 @Idempotent
     * @return Aspect
     */
    @Bean
    public IdempotentAspect idempotentAspect(){
       return new IdempotentAspect();
    }

    /**
     * key 解析器
     * @return KeyResolver
     */
    @Bean
    @ConditionalOnMissingBean(KeyResolver.class)
    public KeyResolver keyResolver(){
        return new ExpressionResolver();
    }
}
