package com.syraven.cloud.service.impl;

import com.syraven.cloud.domain.UserVo;
import com.syraven.cloud.util.RedisKeyUtil;
import com.syraven.cloud.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/5/12 17:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisConfigTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    @Qualifier("secondaryRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate2;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ValueOperations<String, Object> valueOperations;

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    private ListOperations<String, Object> listOperations;

    @Autowired
    private SetOperations<String, Object> setOperations;

    @Autowired
    private ZSetOperations<String, Object> zSetOperations;

    @Resource
    private RedisUtil redisUtil;

    @Test
    @Cacheable(value = "user",key = "123")
    public void testObj() throws Exception{
        UserVo userVo = new UserVo();
        userVo.setAddress("上海");
        userVo.setName("测试dfas");
        userVo.setAge(123);
        ValueOperations<String,Object> operations = redisTemplate2.opsForValue();
        String key = RedisKeyUtil.getKey(UserVo.Table,"name",userVo.getName());
        //redisUtil.expireKey(key,20, TimeUnit.SECONDS);
        operations.set(key,userVo,20, TimeUnit.SECONDS);
        UserVo vo = (UserVo) operations.get(key);
        System.out.println(vo);
    }

    @Resource
    private Redisson redisson;


    public static void main(String[] args) {

        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.0.152:6379").setPassword("Ca@6SwfsbA&C8FGK");
        RedissonClient client = Redisson.create(config);

        //Redisson 限流器示例（RRateLimiter）
        /*RRateLimiter rateLimiter = client.getRateLimiter("myRateLimiter");
        //初始化
        //最大流速 - 每秒钟产生10个令牌
        rateLimiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.SECONDS);

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                try {
                    rateLimiter.acquire();
                    System.out.println("线程" + Thread.currentThread().getId() + "进入数据区：" + System.currentTimeMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }*/

        /*public interface RBloomFilter<T> extends RExpirable {
            boolean add(T var1);    //添加对象

            boolean contains(T var1); //判断对象是否存在

            boolean tryInit(long var1, double var3); //初始化布隆过滤器，var1表示大小，var3表示容错率

            long getExpectedInsertions();  //返回预计插入数量

            double getFalseProbability(); //返回容错率

            long getSize();  /对象插入后，估计插入数量

            int getHashIterations();  //返回hash函数个数

            long count();   //布隆过滤器位数组的大小
        }*/


        //Redis redisson 布隆过滤器（RBloomFilter）
        /*RBloomFilter<String> bloomFilter = client.getBloomFilter("bloom-filter");
        bloomFilter.tryInit(1000,0.03);

        for (int i = 0; i < 1000; i++) {
            bloomFilter.add("天王盖地虎"+i);
        }

        System.out.println("'天王盖地虎 1'是否存在："+bloomFilter.contains("天王盖地虎"+1));
        System.out.println("'海贼王'是否存在："+bloomFilter.contains("海贼王"));
        System.out.println("预计插入数量："+bloomFilter.getExpectedInsertions());
        System.out.println("容错率："+bloomFilter.getFalseProbability());
        System.out.println("hash函数的个数："+bloomFilter.getHashIterations());
        System.out.println("插入对象的个数："+bloomFilter.count());
        System.out.println("布隆过滤器位数组的大小："+bloomFilter.getSize());*/


        RCountDownLatch countDownLatch = client.getCountDownLatch("cuntDownLatch");
        countDownLatch.trySetCount(5);

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            executorService.submit(() ->{
                try {
                    System.out.println("天王盖地虎"+finalI+"开始执行："+System.currentTimeMillis());
                    Thread.sleep(1000);
                    System.out.println("天王盖地虎"+finalI+"开始结束："+System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    countDownLatch.countDown();
                }
            });
        }

        try{
            System.out.println("主线程开始等待："+System.currentTimeMillis());
            countDownLatch.await();
            System.out.println("主线程等待结束："+System.currentTimeMillis());
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("主线程运行结束");
    }
}