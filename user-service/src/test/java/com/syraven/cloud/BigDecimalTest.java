package com.syraven.cloud;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * <<金额的计算BigDecimal类>>
 *
 * @author Raven
 * @date 2020/1/17 15:35
 */
public class BigDecimalTest {

    public static void main(String[] args) {
        double d = 9.84;
        double d2 = 1.22;

        //注意需要使用BigDecimal(String val)构造方法
        BigDecimal bigDecimal = new BigDecimal(Double.toString(d));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(d2));

        //加法
        BigDecimal bigDecimalAdd = bigDecimal.add(bigDecimal2);
        double add = bigDecimalAdd.doubleValue();

        //减法
        BigDecimal bigDecimalSubtraction = bigDecimal.subtract(bigDecimal2);
        double subtract = bigDecimalSubtraction.doubleValue();

        //乘法
        BigDecimal bigDecimalMultiply = bigDecimal.multiply(bigDecimal2);
        double multiply = bigDecimalMultiply.doubleValue();

        //除法
        int scale = 2;//保留2位小数
        BigDecimal bigDecimalDivide = bigDecimal.divide(bigDecimal2,scale,BigDecimal.ROUND_HALF_UP);
        double divide = bigDecimalDivide.doubleValue();

        System.out.println("d+d2 = " + add);
        System.out.println("d-d2 = " + subtract);
        System.out.println("d*d2 = " + multiply);
        System.out.println("d/d2 = " + divide);

        double format = 12343171.6;

        //获取常规数值格式
        NumberFormat number = NumberFormat.getNumberInstance();
        String str = number.format(format);
        System.out.println(str);

        //获取整数数值格式
        NumberFormat integer = NumberFormat.getIntegerInstance();
        str = integer.format(format);//如果带小数会四舍五入到整数12,343,172
        System.out.println(str);

        //获取货币数值格式
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        currency.setMinimumFractionDigits(2);//设置数的小数部分所允许的最小位数(如果不足后面补0)
        currency.setMaximumFractionDigits(4);//设置数的小数部分所允许的最大位数(如果超过会四舍五入)
        str = currency.format(format);//￥12,343,171.60
        System.out.println(str);

        //获取显示百分比的格式
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMinimumFractionDigits(2);//设置数的小数部分所允许的最小位数(如果不足后面补0)
        percent.setMaximumFractionDigits(3);//设置数的小数部分所允许的最大位数(如果超过会四舍五入)
        str = percent.format(format);
        System.out.println(str);
        

    }
}