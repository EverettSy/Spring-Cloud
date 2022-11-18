package com.syraven.cloud.service.impl;

import cn.hutool.json.JSONUtil;
import com.syraven.cloud.common.constants.RedisKeyPrefixConst;
import com.syraven.cloud.domain.Product;
import com.syraven.cloud.mapper.ProductMapper;
import com.syraven.cloud.service.ProductService;
import com.syraven.cloud.utlis.LockUtils;
import com.syraven.cloud.utlis.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-11-10 16:36
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    private final RedisUtils redisUtils;

    private final LockUtils lockUtils;

    public static final Integer PRODUCT_CACHE_TIMEOUT = 60 * 60 * 24;
    public static final String EMPTY_CACHE = "{}";
    public static final String LOCK_PRODUCT_HOT_CACHE_CREATE_PREFIX = "lock:product:hot_cache_create:";
    public static final String LOCK_PRODUCT_UPDATE_PREFIX = "lock:product:update:";
    protected static Map<String, Product> productMap = new HashMap<>();


    @Override
    public Product create(Product product) {
        productMapper.insert(product);
        redisUtils.set(RedisKeyPrefixConst.PRODUCT_CACHE + product.getId(), JSONUtil.toJsonStr(product));
        return product;
    }

    @Override
    public Product update(Product product) {
        Product productResult = null;
        RReadWriteLock productUpdateLock = lockUtils.readWriteLock(LOCK_PRODUCT_UPDATE_PREFIX + product.getId());
        RLock writeLock = productUpdateLock.writeLock();
        //加分布式写锁解决缓存双写不一致问题
        writeLock.lock();

        try {
            productMapper.updateById(product);
            productResult = productMapper.selectById(product.getId());
            redisUtils.set(RedisKeyPrefixConst.PRODUCT_CACHE + product.getId(), JSONUtil.toJsonStr(product));
        } finally {
            writeLock.unlock();
        }
        return productResult;
    }

    @Override
    public Product get(String productId) {
        Product product = null;
        String productCacheKey = RedisKeyPrefixConst.PRODUCT_CACHE + productId;

        //从缓存里查数据
        product = getProductFromCache(productCacheKey);
        if (product != null) {
            return product;
        }
        //加分布式锁解决热点缓存并发重建问题
        RLock productHotCacheCreateLock = lockUtils.getLock(LOCK_PRODUCT_HOT_CACHE_CREATE_PREFIX + productId);
        productHotCacheCreateLock.lock();
        // 这个优化谨慎使用，防止超时导致的大规模并发重建问题
        try {
            productHotCacheCreateLock.tryLock(0,1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        try {
            product = getProductFromCache(productCacheKey);
            if (product != null) {
                return product;
            }

            RReadWriteLock productUpdateLock = lockUtils.readWriteLock(LOCK_PRODUCT_UPDATE_PREFIX + productId);
            RLock rLock = productUpdateLock.readLock();
            //加分布式读锁解决缓存双写不一致问题
            rLock.lock();
            try {
                product = productMapper.selectById(productId);
                if (product != null) {
                    redisUtils.set(productCacheKey, JSONUtil.toJsonStr(product), genProductCacheTimeout(), TimeUnit.SECONDS);

                }else {
                    //设置空缓存解决缓存穿透问题
                    redisUtils.set(productCacheKey, EMPTY_CACHE, genEmptyCacheTimeout(), TimeUnit.SECONDS);

                }
            } finally {
                rLock.unlock();
            }
        } finally {
            productHotCacheCreateLock.unlock();
        }
        //加分布式读锁解决缓存双写不一致问题
        return product;
    }

    private Integer genProductCacheTimeout()  {
       //加随机超时机制解决缓存批量失效(击穿)问题
        return PRODUCT_CACHE_TIMEOUT+ new SecureRandom().nextInt(5) * 60 * 60;
    }


    private Integer genEmptyCacheTimeout() {
        return 60 + new SecureRandom().nextInt(30);
    }


    private Product getProductFromCache(String productCacheKey) {
        Product product = null;
        //多级缓存查询，jvm级别缓存可以交给单独的热点缓存系统统一维护，有变动推送到各个web应用系统自行更新
        product = productMap.get(productCacheKey);
        if (product != null) {
            return product;
        }
        String productStr = redisUtils.getStr(productCacheKey);
        if (StringUtils.isNotEmpty(productStr)) {
            if (EMPTY_CACHE.equals(productStr)) {
                redisUtils.expire(productCacheKey,genEmptyCacheTimeout(), TimeUnit.SECONDS);
                return new Product();
            }
            product = JSONUtil.toBean(productStr, Product.class);
            //缓存续期
            redisUtils.expire(productCacheKey,genProductCacheTimeout(), TimeUnit.SECONDS);
            productMap.put(productCacheKey, product);
        }
        return product;
    }
}
