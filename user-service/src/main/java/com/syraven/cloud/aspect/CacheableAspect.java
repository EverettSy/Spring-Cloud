package com.syraven.cloud.aspect;

import com.syraven.cloud.annotation.ExtCacheable;
import com.syraven.cloud.util.RedisUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/6/24 16:44
 */
@Component
@Aspect
public class CacheableAspect {

    @Autowired
    private RedisUtils redisUtils;

    @Pointcut("@annotation(com.syraven.cloud.annotation.ExtCacheable)")
    public void annotationPointcut(){
    }

    @Around("annotationPointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable{

        //获取当前访问的class
        Class<?> className = joinPoint.getTarget().getClass();
        //获取访问的方法名
        String methodName = joinPoint.getSignature().getName();
        //得到方法的参数的类型
        Class<?>[] argClass = ((MethodSignature)joinPoint.getSignature()).getParameterTypes();
        Object[] args = joinPoint.getArgs();
        String key = "";
        long exireTime = 1800L;
        TimeUnit timeUnit = TimeUnit.MINUTES;

        //得到访问的方法对象
        try {
            Method method = className.getMethod(methodName,argClass);
            method.setAccessible(true);
            //判断是否存在@ExtCacheable注解
            if (method.isAnnotationPresent(ExtCacheable.class)){
                ExtCacheable annotation = method.getAnnotation(ExtCacheable.class);
                key = getRedisKey(args,annotation);
                exireTime = getExpireTime(annotation);
                timeUnit = getTimeUnit(annotation);
            }
        } catch (NoSuchMethodException e) {
            throw  new RuntimeException("redis缓存注解参数异常",e);
        }

        //获取缓存是否存在
        boolean hasKey = redisUtils.hasKey(key);
        if (hasKey){
            return redisUtils.get(key);
        }else {
            //执行原方法（java反射执行method获取结果）
            Object res = joinPoint.proceed();
            //设置缓存
            redisUtils.set(key,res);
            //设置过期时间
            redisUtils.expire(key,exireTime,timeUnit);
            return res;
        }
    }

    private long getExpireTime(ExtCacheable annotation) {
        return annotation.expireTime();
    }

    private TimeUnit getTimeUnit(ExtCacheable annotation) {
        return annotation.timeUnit();
    }

    private String getRedisKey(Object[] args,ExtCacheable annotation) {
        String primalKey = annotation.key();
        //获取#p0...集合
        List<String> keyList = getKeyParsList(primalKey);
        for (String keyName : keyList) {
            int keyIndex = Integer.parseInt(keyName.toLowerCase().replace("#p", ""));
            Object parValue = args[keyIndex];
            primalKey = primalKey.replace(keyName, String.valueOf(parValue));
        }
        return primalKey.replace("+","").replace("'","");
    }

    // 获取key中#p0中的参数名称
    private static List<String> getKeyParsList(String key) {
        List<String> ListPar = new ArrayList<String>();
        if (key.indexOf("#") >= 0) {
            int plusIndex = key.substring(key.indexOf("#")).indexOf("+");
            int indexNext = 0;
            String parName = "";
            int indexPre = key.indexOf("#");
            if (plusIndex > 0) {
                indexNext = key.indexOf("#") + key.substring(key.indexOf("#")).indexOf("+");
                parName = key.substring(indexPre, indexNext);
            } else {
                parName = key.substring(indexPre);
            }
            ListPar.add(parName.trim());
            key = key.substring(indexNext + 1);
            if (key.indexOf("#") >= 0) {
                ListPar.addAll(getKeyParsList(key));
            }
        }
        return ListPar;
    }

}