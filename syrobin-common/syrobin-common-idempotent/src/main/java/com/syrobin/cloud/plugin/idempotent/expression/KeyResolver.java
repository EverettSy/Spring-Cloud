package com.syrobin.cloud.plugin.idempotent.expression;

import com.syrobin.cloud.plugin.idempotent.annotation.Idempotent;
import org.aspectj.lang.JoinPoint;

/**
 * @ClassName: KeyResolver
 * @Description: 唯一标志处理器
 * @Author syrobin
 * @Date 2021-11-26 4:59 下午
 * @Version V1.0
 */
public interface KeyResolver {

    /**
     * 解析key
     * @param idempotent 接口注解标识
     * @param point 接口切点信息
     * @return 处理结果
     */
    String resolver(Idempotent idempotent, JoinPoint point);
}
