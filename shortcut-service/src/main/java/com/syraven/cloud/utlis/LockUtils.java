package com.syraven.cloud.utlis;

import cn.hutool.core.map.MapUtil;
import org.redisson.RedissonMultiLock;
import org.redisson.api.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author syrobin
 * @version v1.0
 * @description: redis工具类
 * @date 2022-11-08 09:28
 */
public class LockUtils {

    private final RedissonClient redissonClient;

    public LockUtils(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 获取锁
     * @param key
     * @return
     */
    public RLock getLock(String key){
        return redissonClient.getLock(key);
    }

    /**
     * 可重入锁（Reentrant Lock）
     * @param key
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public boolean tryLock(String key, long waitTime,long leaseTime, TimeUnit unit) throws InterruptedException {
        RLock lock = redissonClient.getLock(key);
        return lock.tryLock(waitTime,leaseTime, unit);
    }

    /**
     * 异步可重入锁
     * @param key
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public boolean asyncTryLock(String key, long waitTime,long leaseTime, TimeUnit unit) throws ExecutionException, InterruptedException {
        RLock lock = redissonClient.getLock(key);
        lock.lockAsync();
        lock.lockAsync(leaseTime, unit);
        return lock.tryLockAsync(waitTime,leaseTime, unit).get();
    }


    /**
     * 公平锁（Fair Lock）
     * @param key
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public boolean fairLock(String key, long waitTime,long leaseTime, TimeUnit unit) throws InterruptedException {
        RLock lock = redissonClient.getFairLock(key);
        lock.lock();
        lock.lock(leaseTime, unit);
        return lock.tryLock(waitTime,leaseTime, unit);
    }


    /**
     * 联锁（MultiLock）
     * @param key1
     * @param key2
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public boolean multiLock(String key1,String key2, long waitTime,long leaseTime, TimeUnit unit) throws InterruptedException {
        RLock lock1 = redissonClient.getLock(key1);
        RLock lock2 = redissonClient.getLock(key2);
        RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2);
        // 同时加锁：lock1 lock2 lock3, 所有的锁都上锁成功才算成功。
        lock.lock();
        return lock.tryLock(waitTime,leaseTime, unit);
    }


    /**
     * 红锁（RedLock）
     * @param key1
     * @param key2
     * @param key3
     * @param waitTime
     * @param leaseTime
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public boolean redLock(String key1,String key2,String key3 ,long waitTime,long leaseTime, TimeUnit unit) throws InterruptedException {
        RLock lock1 = redissonClient.getLock(key1);
        RLock lock2 = redissonClient.getLock(key2);
        RLock lock3 = redissonClient.getLock(key3);
        RedissonMultiLock lock = new RedissonMultiLock(lock1, lock2, lock3);
        // 同时加锁：lock1 lock2 lock3, 红锁在大部分节点上加锁成功就算成功。
        lock.lock();
        return lock.tryLock(waitTime,leaseTime, unit);
    }

    /**
     * 读写锁（ReadWriteLock）
     * @param key
     * @return
     */
    public RReadWriteLock readWriteLock(String key) {
        return redissonClient.getReadWriteLock(key);
    }

    /**
     * 获取getBuckets 对象
     * @return RBuckets 对象
     */
    public RBuckets getBuckets(){
        return redissonClient.getBuckets();
    }

    /**
     * 读取缓存中的字符串，永久有效
     * @param key 缓存key
     * @return 字符串
     */
    public String getStr(String key){
        return getBuckets().get(key).toString();
    }

    // ---------------- 批量操作 ------------------------
    public RBatch createBatch(){
        return redissonClient.createBatch();
    }

    /**
     * 批量移除缓存
     * @param keys 缓存key对象
     */
    public void deleteBatch(String... keys){
        if (null == keys){
            return;
        }
        redissonClient.getKeys().delete(keys);
    }


    /**
     * 批量缓存字符串，缺点：不可以设置过期时间
     * @param map 缓存key-value
     */
    public void setMassStrings(Map<String, String> map){
        if (MapUtil.isEmpty(map)) {
            return;
        }
        RBuckets buckets = getBuckets();
        // 同时保存全部通用对象桶。
        buckets.set(map);

    }

    /**
     * 批量缓存字符串，支持过期
     * @param map 缓存key-value
     * @param leaseTime 缓存有效期，必传
     */
    public void setMassStrings(Map<String, String> map, long leaseTime, TimeUnit unit){
        if (MapUtil.isEmpty(map)) {
            return;
        }
        RBatch batch = createBatch();
        map.forEach((key, value) -> batch.getBucket(key).setAsync(value, leaseTime, unit));
        batch.execute();
    }


}
