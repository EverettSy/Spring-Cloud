package com.syraven.cloud.configuration;

import com.syraven.cloud.configuration.bloom.BloomFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * spring 异步工具配置类
 *
 * @author SyRAVEN
 * @since 2021-07-12 17:33
 */
@EnableAsync
@Component
@Slf4j
public class AsyncJob {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private BloomFilter bloomFilter;

    /**
     * 自定义异步线城池
     *
     * @return
     */
    @Bean
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("bloom-Executor");
        //这个任务基本没有计算逻辑，全是和redis的io操作，所以属于IO密集型任务，最大线程数设置应为 cpu核数*2
        int thread = Runtime.getRuntime().availableProcessors() * 2;
        //最大线程数
        executor.setMaxPoolSize(thread);
        //核心池大小(右移运算符，num >> 1,相当于num除以2)
        executor.setCorePoolSize(thread >> 1);
        //空闲线程的保留时间
        executor.setKeepAliveSeconds(5);
        //不设置队列最大上限
        executor.setQueueCapacity(Integer.MAX_VALUE);
        return executor;
    }

    public void add2RedisAndBloomFilter(String shortCut, String url) {
        log.info("正在执行异步任务，添加[shortCut]={},[url]={} 到布隆过滤器以及redis中....", shortCut, url);
        //放到redis里面
        redisTemplate.opsForValue().set(shortCut, url);
        // 添加到布隆过滤器
        bloomFilter.addByBloomFilter(url);
        redisTemplate.opsForValue().set(url, shortCut);
    }
}
