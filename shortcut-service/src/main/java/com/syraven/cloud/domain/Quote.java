package com.syraven.cloud.domain;

import lombok.Data;

/**
 * @author syrobin
 * @version v1.0
 * @description: 报价
 * @date 2022-03-10 4:48 PM
 */
@Data
public class Quote {

    /**
     * 商品名称
     */
    private final String shopName;
    /**
     * 价格
     */
    private final double price;
    /**
     * 折扣代码
     */
    private final Discount.Code discountCode;

    public static Quote parse(String s){
        String[] split = s.split(":");
        String shopName = split[0];
        double price = Double.parseDouble(split[1]);
        Discount.Code discountCode = Discount.Code.valueOf(split[2]);
        return new Quote(shopName,price,discountCode);
    }
}
