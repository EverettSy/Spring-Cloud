package com.syrobin.cloud.accountservice.dao;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-03-01 11:12 AM
 */
public interface AccountDao {

    /**
     * 扣减账户余额
     * @param userId 用户id
     * @param payAmount 金额
     */
    void decrease(@Param("userId") Long userId, @Param("payAmount") BigDecimal payAmount);
}
