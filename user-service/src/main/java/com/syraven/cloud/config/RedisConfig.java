package com.syraven.cloud.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;


/**
 * <<reids 相关bean的配置>>
 *
 * @author Raven
 * @date 2020/4/8 17:25
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.host}")
    private String redisUrl;
    @Value("${spring.redis2.host}")
    private String redisUrl2;
    //缓存管理器
    /*@Bean
    public CacheManager cacheManager(@Qualifier("redisConnectionFactory") RedisConnectionFactory factory) {
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory).build();
        return cacheManager;
    }*/

    //自定义缓存key生成策略
    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuffer sb = new StringBuffer();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object object : params) {
                    sb.append(object.toString());
                }
                System.out.println("调用Redis生成key" + sb.toString());
                return sb.toString();
            }
        };
    }

    /**
     * 配置 lettuce连接池
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis.lettuce.pool")
    public GenericObjectPoolConfig redisPool() {
        return new GenericObjectPoolConfig<>();
    }


    /**
     * 配置第一个数据源
     *
     * @return
     */
    @Bean("redisClusterConfig")
    @Primary
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisStandaloneConfiguration redisClusterConfig() {
        return new RedisStandaloneConfiguration(redisUrl);
    }

    /**
     * 配置第二个数据源
     *
     * @return
     */
    @Bean("secondaryRedisClusterConfig")
    @ConfigurationProperties(prefix = "spring.redis2")
    public RedisStandaloneConfiguration secondaryRedisClusterConfig() {
        return new RedisStandaloneConfiguration(redisUrl2);
    }


    /**
     * 配置第一个数据源的连接工厂
     * 这里注意：需要添加@Primary 指定bean的名称，目的是为了创建两个不同名称的LettuceConnectionFactory
     *
     * @param poolConfig
     * @param redisClusterConfig
     * @return
     */
    @Bean("factory")
    @Primary
    public LettuceConnectionFactory factory(GenericObjectPoolConfig poolConfig, @Qualifier("redisClusterConfig")RedisStandaloneConfiguration redisClusterConfig) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(poolConfig).build();
        return new LettuceConnectionFactory(redisClusterConfig, clientConfiguration);
    }

    /**
     * 配置第二个数据源的连接工厂
     *
     * @param poolConfig
     * @param secondaryRedisClusterConfig
     * @return
     */
    @Bean("factory2")
    public LettuceConnectionFactory factory2(GenericObjectPoolConfig poolConfig, @Qualifier("secondaryRedisClusterConfig")RedisStandaloneConfiguration secondaryRedisClusterConfig) {
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(poolConfig).build();
        return new LettuceConnectionFactory(secondaryRedisClusterConfig, clientConfiguration);
    }


    /**
     * 配置第一个数据源的RedisTemplate
     * 注意：这里指定使用名称=factory 的 RedisConnectionFactory
     * 并且标识第一个数据源是默认数据源 @Primary
     *
     * @param factory
     * @return
     */
    @Bean("redisTemplate")
    @Primary
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("factory") RedisConnectionFactory factory) {
        return getStringRedisTemplate(factory);
    }

    @Bean("secondaryRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate2(@Qualifier("factory2") RedisConnectionFactory factory2) {
        return getStringRedisTemplate(factory2);
    }


    /**
     * 设置序列化方式
     *
     * @param factory
     * @return
     */
    public RedisTemplate<String, Object> getStringRedisTemplate(RedisConnectionFactory factory) {

        // 配置连接工厂
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = getJackson2JsonRedisSerializer();
        // 值采用json序列化
        template.setValueSerializer(jsonRedisSerializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jsonRedisSerializer);
        template.afterPropertiesSet();

        return template;

    }

    private Jackson2JsonRedisSerializer<Object> getJackson2JsonRedisSerializer() {
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        //指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        jsonRedisSerializer.setObjectMapper(om);
        return jsonRedisSerializer;
    }

    /**
     * 对hash类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对字符串类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }
}