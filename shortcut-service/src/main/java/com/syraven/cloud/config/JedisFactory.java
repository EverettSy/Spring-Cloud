package com.syraven.cloud.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cglib.proxy.Proxy;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.commands.JedisCommands;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-06-25 18:21
 */
public class JedisFactory {

    /**
     * 创建单机连接池
     *
     * @param properties
     * @return
     */
    public static JedisPool createJedisPool(RedisProperties properties) {
        JedisPoolConfig config = setConfig(properties);
        return new JedisPool(config, properties.getHost(), properties.getPort(), 3000,
                properties.getPassword(), properties.getDatabase());
    }

    /**
     * 创建单机客户端
     *
     * @param properties
     * @return
     */
    public static JedisCommands createJedis(RedisProperties properties) {
        JedisPoolConfig config = setConfig(properties);
        JedisPool jedisPool = new JedisPool(config, properties.getHost(), properties.getPort(), 3000,
                properties.getPassword(), properties.getDatabase());
        return createJedis(jedisPool);
    }

    /**
     * 创建单机客户端
     *
     * @param pool
     * @return
     */
    public static JedisCommands createJedis(JedisPool pool) {
        JedisHandler handler = new JedisHandler(pool);
        return (JedisCommands) Proxy.newProxyInstance(JedisCommands.class.getClassLoader(), new Class[]{JedisCommands.class}, handler);
    }

    /**
     * 创建集群客户端
     *
     * @param properties
     * @return
     */
    public static JedisCluster createCluster(RedisProperties properties) {
        List<String> nodeStr = properties.getCluster().getNodes();
        Set<HostAndPort> nodes = new HashSet<>();
        for (String node : nodeStr) {
            String[] hostPort = node.split(":");
            nodes.add(new HostAndPort(hostPort[0], Integer.valueOf(hostPort[1])));
        }
        JedisPoolConfig config = setConfig(properties);
        // 利用上面的集群节点nodes和poolConfig，创建redis集群连接池，并获取一个redis连接
        return new JedisCluster(nodes, 3000, 3000, 5, properties.getPassword(), config);
    }

    /**
     * 获取配置
     *
     * @param properties
     * @return
     */
    private static JedisPoolConfig setConfig(RedisProperties properties) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(properties.getJedis().getPool().getMaxIdle());
        config.setMaxIdle(properties.getJedis().getPool().getMaxIdle());
        config.setMinIdle(properties.getJedis().getPool().getMinIdle());
        config.setMaxWaitMillis(3000);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        return config;
    }
}
