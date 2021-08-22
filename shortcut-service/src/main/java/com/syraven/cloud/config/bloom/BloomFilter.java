package com.syraven.cloud.config.bloom;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 布隆过滤器
 * @author SyRAVEN
 * @since 2021-07-08 19:46
 */
@Component
public class BloomFilter {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private BloomFilterHelper bloomFilterHelper;


    /**
     * 根据给定的布隆过滤器添加值
     * @param value
     */
    public void addByBloomFilter(String value){
        Preconditions.checkArgument(value != null,"value不能为空");
        long[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (long i : offset) {
            redisTemplate.opsForValue().setBit(bloomFilterHelper.getBfKey(), i,true);
        }
    }

    /**
     * 根据给定的布隆过滤器判断值是否存在
     * @param value
     * @return
     */
    public boolean includeByBloomFilter(String value){
        Preconditions.checkArgument(value != null,"value不能为空");
        long[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (long i : offset) {
            Boolean include;
            if ((include = redisTemplate.opsForValue().getBit(bloomFilterHelper.getBfKey(), i)) == null || !include){
                return false;
            }
        }
        return true;
    }
}
