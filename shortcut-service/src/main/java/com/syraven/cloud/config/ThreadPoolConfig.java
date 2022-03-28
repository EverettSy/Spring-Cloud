package com.syraven.cloud.config;

import cn.hippo4j.core.executor.DynamicThreadPool;
import cn.hippo4j.core.executor.support.ThreadPoolBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author syrobin
 * @version v1.0
 * @description: 线程池配置
 * @date 2022-03-23 23:42
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    @DynamicThreadPool
    public ThreadPoolExecutor dynamicThreadPoolExecutor() {
        String consumeThreadPoolId = "message-consume";
        return ThreadPoolBuilder.builder()
                .threadFactory(consumeThreadPoolId)
                .waitForTasksToCompleteOnShutdown(true)
                .awaitTerminationMillis(5000L)
                .taskDecorator(new ContextCopyingDecorator())
                .dynamicPool()
                .build();
    }

}
