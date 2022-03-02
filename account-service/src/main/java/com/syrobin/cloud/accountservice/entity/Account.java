package com.syrobin.cloud.accountservice.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-03-01 11:14 AM
 */
@Data
public class Account {

    private Long id;

    /**用户id*/
    private Long userId;

    /**总额度*/
    private BigDecimal total;

    /**已用额度*/
    private BigDecimal used;

    /**剩余额度*/
    private BigDecimal balance;
    /**更新时间*/
    private Date lastUpdateTime;
}