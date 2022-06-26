package com.syraven.cloud.config;

import org.springframework.cglib.proxy.InvocationHandler;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Method;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-06-25 18:23
 */
public class JedisHandler implements InvocationHandler {
    private JedisPool pool;

    public JedisHandler(JedisPool pool) {
        this.pool = pool;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Jedis jedis = null;
        try {
            System.out.println("进入代理");
            jedis = pool.getResource();
            return method.invoke(jedis, args);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
