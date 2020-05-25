package com.syraven.cloud.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.syraven.cloud.domain.CommonResult;
import com.syraven.cloud.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/1/19 13:57
 */
@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private RestTemplate restTemplate;
    @Value("${service-url.user-service}")
    private String userServiceUrl;

    @HystrixCommand(fallbackMethod = "getDefaultUser")
    public CommonResult getUser(Integer id){
        return restTemplate.getForObject(userServiceUrl+"/user/{1}",CommonResult.class,id);
    }

    public CommonResult getDefaultUser(Integer id){
        User defaultUser = new User(1,"defaultUser","123456");
        return new CommonResult<>(defaultUser);
    }

    @HystrixCommand(fallbackMethod = "getDefaultUser2",ignoreExceptions = {NullPointerException.class})
    public CommonResult getUserException(Integer id){
        if (id == 1){
            throw new IndexOutOfBoundsException();
        }else if (id == 2){
            throw new NullPointerException();
        }
        return restTemplate.getForObject(userServiceUrl +"/user/{id}",CommonResult.class,id);
    }

    public CommonResult getDefaultUser2(@PathVariable Integer id,Throwable e){
        logger.error("getDefaultUser2 id:{},throwable class:{}",id,e.getClass());
        User defaultUser = new User(2,"defaultUser2","123456");
        return new CommonResult<>(defaultUser);
    }

    /**
     * 设置命令、分组及线程池名称
     *
     * fallbackMethod：指定服务降级处理方法；
     * ignoreExceptions：忽略某些异常，不发生服务降级；
     * commandKey：命令名称，用于区分不同的命令；
     * groupKey：分组名称，Hystrix会根据不同的分组来统计命令的告警及仪表盘信息；
     * threadPoolKey：线程池名称，用于划分线程池。
     *
     * @param id
     * @return
     */

    @HystrixCommand(fallbackMethod = "getDefaultUser",
            commandKey = "getUserCommand",
            groupKey = "getUserGroup",
            threadPoolKey = "getUserThreadPool"
    )
    public CommonResult getUserCommand(@PathVariable Integer id){
        logger.info("getUserCommand id:{}",id);
        return restTemplate.getForObject(userServiceUrl+"/user/{id}",CommonResult.class,id);
    }

    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(fallbackMethod = "getDefaultUser",commandKey = "getUserCache")
    public CommonResult getUserCache(Integer id){
        logger.info("getUserCache id:{}",id);
        return restTemplate.getForObject(userServiceUrl + "/user/{id}",CommonResult.class,id);
    }

    /**
     * 为缓存生成key的方法
     *
     * @param id
     * @return
     */
    public String getCacheKey(Integer id){
        return String.valueOf(id);
    }

    @CacheRemove(commandKey = "getUserCache",cacheKeyMethod = "getCacheKey")
    @HystrixCommand
    public CommonResult removeCache(Integer id){
        logger.info("removeCache id:{}",id);
        return restTemplate.postForObject(userServiceUrl+"/user/{1}",null,CommonResult.class,id);
    }
}