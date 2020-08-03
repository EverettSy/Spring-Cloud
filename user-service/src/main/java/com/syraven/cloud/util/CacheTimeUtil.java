package com.syraven.cloud.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * <<随机数生成工具类>>
 *
 * @author Raven
 * @date 2020/6/29 11:53
 */
public class CacheTimeUtil {

    public static int generateRandomCacheTime(int origin,int bound){
        //万一origin和bound一样，bound默认加300
        if (origin == bound){
            return ThreadLocalRandom.current().nextInt(origin, (bound+300));
        }
        return ThreadLocalRandom.current().nextInt(origin, bound);
    }

}