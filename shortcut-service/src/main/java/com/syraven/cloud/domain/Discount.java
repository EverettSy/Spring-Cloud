package com.syraven.cloud.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.syraven.cloud.domain.Shop.delay;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-03-10 4:42 PM
 */
public class Discount {
    public enum Code{
        NONE(0),
        SILVER(5),
        GOLD(10),
        PLATINUM(15),
        DIAMOND(20)
        ;

        /**
         * 百分比
         */
        private final int percentage;

        Code(int percentage){
            this.percentage = percentage;
        }
    }

    public static String applyDiscount(Quote quote){
        return quote.getShopName() + "价格是 "+
                //将折扣代码应用于商品最初的原始价格
                Discount.apply(quote.getPrice(),quote.getDiscountCode());
    }
    private static double apply(double price, Code code) {
        //模拟 Discount 服务响应的延迟
        delay();
        return userBigDecimal(price * (100 - code.percentage) /100);
    }


    public static double userBigDecimal(double n) {
        BigDecimal bigDecimal = BigDecimal.valueOf(n);
        return bigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
