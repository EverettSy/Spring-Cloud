package com.syrobin.cloud.plugin.idempotent.aspect;

import com.syrobin.cloud.plugin.idempotent.annotation.Idempotent;
import com.syrobin.cloud.plugin.idempotent.exception.IdempotentException;
import com.syrobin.cloud.plugin.idempotent.expression.KeyResolver;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.Redisson;
import org.redisson.api.RMapCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: IdempotentAspect
 * @Description: 幂等切面
 * @Author syrobin
 * @Date 2021-11-26 4:43 下午
 * @Version V1.0
 */
@Aspect
@Slf4j
public class IdempotentAspect {

    private static final ThreadLocal<Map<String, Object>> THREAD_CACHE = ThreadLocal.withInitial(HashMap::new);

    private static final String RMAPCACHE_KEY = "idempotent";

    private static final String KEY = "key";

    private static final String DELKEY = "delKey";

    @Resource
    private  Redisson redisson;

    @Autowired
    private  KeyResolver keyResolver;

    /**
     * 切点
     */
    @Pointcut("@annotation(com.syrobin.cloud.plugin.idempotent.annotation.Idempotent)")
    public void pointCut(){

    }

    @Before("pointCut()")
    public void beforePointCut(JoinPoint joinPoint){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = requestAttributes.getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (!method.isAnnotationPresent(Idempotent.class)){
            return;
        }
        Idempotent idempotent = method.getAnnotation(Idempotent.class);
        String key;
        // 若没有配置 幂等 标识编号，则使用 url + 参数列表作为区分
        if (!StringUtils.hasLength(idempotent.key())){
            String url = request.getRequestURI().toString();
            String argString = Arrays.asList(joinPoint.getArgs()).toString();
            key = url + argString;
        }else {
            //使用jstl 规则区分
            key = keyResolver.resolver(idempotent,joinPoint);
        }

        long expireTime = idempotent.expireTime();
        String info = idempotent.info();
        TimeUnit timeUnit = idempotent.timeUnit();
        boolean delKey = idempotent.delKey();

        //
        RMapCache<String,Object> rMapCache = redisson.getMapCache(RMAPCACHE_KEY);
        String value = LocalDateTime.now().toString().replace("T"," ");
        Object v1;
        if (null != rMapCache.get(key)){
            //
            throw new IdempotentException("[idempotent]:"+ info);
        }
        synchronized (this){
            v1 = rMapCache.putIfAbsent(key,value,expireTime,timeUnit);
            if(null != v1){
                throw new IdempotentException("[idempotent]:"+ info);
            }
            else {
                log.info("[idempotent]:has stored key={},value={},expireTime={}{},now={}",
                        key, value, expireTime,
                        timeUnit, LocalDateTime.now().toString());
            }
        }

        Map<String, Object> map = THREAD_CACHE.get();
        map.put(KEY, key);
        map.put(DELKEY,delKey);
    }

    @After("pointCut()")
    public void afterPointCut(JoinPoint joinPoint){
       Map<String, Object> map = THREAD_CACHE.get();
       if (CollectionUtils.isEmpty(map)){
           return;
       }
       RMapCache<Object,Object> mapCache = redisson.getMapCache(RMAPCACHE_KEY);
       if (mapCache.size() == 0){
           return;
       }

       String key = map.get(KEY).toString();
       boolean delKey = (boolean) map.get(DELKEY);
        if (delKey) {
            mapCache.fastRemove(key);
            log.info("[idempotent]:has removed key={}", key);
        }
        THREAD_CACHE.remove();

    }


}
