package com.syraven.cloud;

import java.util.Scanner;

/**
 * @ClassName: SPITest
 * @Description:
 * @Author syrobin
 * @Date 2021-11-16 5:33 下午
 * @Version V1.0
 */
public class SPITest extends SpringTest {

    public static void main(String[] args) {

        //SPI 获取bean
       /* ApplicationContext ctx = new AnnotationConfigApplicationContext(ConfigA.class);
        ServiceInterface bean = ctx.getBean(ServiceInterface.class);
        bean.test();*/
        Scanner input = new Scanner(System.in);
        System.out.print("请分别输入这三个数：");
        int max = input.nextInt(), mid = input.nextInt(), min = input.nextInt();

        if (max < mid) {
            //交换 max 和 mid 的值
            int temp = max;
            max = mid;
            mid = temp;
        }
        if (max < min) {
            //交换 max 和 min 的值
            int temp = max;
            max = min;
            min = temp;
        }
        if (mid < min) {
            //交换 mid 和 min 的值
            int temp = mid;
            mid = min;
            min = temp;
        }

        System.out.print("您输入的这3个数，由小到大排序为：" + min + "，" + mid + "，" + max);
        input.close();

        //也可以采用将输入的三个数字放在一维数组中，然后对数组进行排序输出（可以参考 JavaExtend03 中的排序方法）

    }


}
