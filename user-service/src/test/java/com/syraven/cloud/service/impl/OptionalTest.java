package com.syraven.cloud.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2021/1/29 17:18
 */
public class OptionalTest {

    public static void main(String[] args) {

        /**
         * Optional.of()
         *
         * 方法作用： 为指定的值创建一个指定非 null 值的 Optional。
         *
         * 方法描述： of 方法通过工厂方法创建 Optional 实例，需要注意的是传入的参数不能为 null，否则抛出 NullPointerException。
         *
         * 返回类型： Optional
         */
        Optional<String> optional = Optional.of("mysql");
        System.out.println(optional.toString());
        Optional<String> optional1 = Optional.of("/");
        System.out.println(optional1);

        /**
         * Optional.ofNullable()
         *
         * 方法作用： 为指定的值创建一个 Optional 对象，如果指定的参数为 null，不抛出异常，直接则返回一个空的 Optional 对象。
         *
         * 方法描述： ofNullable 方法是和 of 方式一样，都是用于创建 Optional 对象，只是传入的参数 null 时，会返回一个空的 Optional 对象，而不会抛出 NullPointerException 异常。
         *
         * 返回类型： Optional
         */
        Optional<String> optional2 = Optional.ofNullable("mysql");
        Optional<String> optional21 = Optional.ofNullable(null);
        System.out.println(optional21);

        /**
         * 对象方法 isPresent()
         *
         * 方法作用： 如果值存在则方法会返回 true，否则返回 false。
         *
         * 方法描述： 该方法其实就是用于判断创建 Optional 时传入参数的值是否为空，实现代码就简单一行，即 value != null 所以如果不为空则返回 true，否则返回 false。
         *
         * 返回类型： boolean
         *
         */
        Optional<String> optional3 = Optional.ofNullable("mysql");
        System.out.println("传入正常值返回："+optional3.isPresent());

        Optional<String> optional4 = Optional.ofNullable(null);
        System.out.println("传入正常值返回："+optional4.isPresent());

        /**
         * 对象方法 get()
         *
         * 方法作用： 如果 Optional 有值则将其返回，否则抛出 NoSuchElementException 异常。
         *
         * 方法描述： get 方法内部实现其实就是判断 Otpional 对象中的 value 属性是否为 null，如果是就抛出 NoSuchElementException 异常，否则返回这个 value 值。
         *
         * 返回类型： T
         *
         */
        System.out.println(optional3.get());
        //System.out.println(optional4.get());

        /**
         * 对象方法 ifPresent()
         *
         * 方法作用： 如果值存在则使用该值调用 consumer , 否则不做任何事情。
         *
         * 方法描述： 该方法 ifPresent(Consumer<? super T> consumer) 中参数接收的是 Consumer 类，它包含一个接口方法 accept()，该方法能够对传入的值进行处理，但不会返回结果。这里传入参数可以传入 Lamdda 表达式或 Consumer 对象及实现 Consumer 接口的类的对象。
         *
         */

        // 创建 Optional 对象，然后调用 Optional 对象的 ifPresent 方法，传入 Lambda 表达式
        Optional optional6 = Optional.ofNullable("mydlq1");
        optional6.ifPresent(value -> {
            System.out.println("Optional 的值为：" + value);
        });

        Optional optional7 = Optional.ofNullable("mysql2");
        Consumer<String> consumer = (Consumer) value -> System.out.println("Optional 的值为：" + value);
        optional7.ifPresent(consumer);


        /**
         * 对象方法 orElse()
         *
         * 方法作用： 如果该值存在就直接返回， 否则返回指定的其它值。
         *
         * 方法描述： orElse 方法实现很简单，就是使用三目表达式对传入的参数值进行 null 验证，即 value != null ? value : other; 如果为 null 则返回 true，否则返回 false。
         *
         * 返回类型： T
         */
        // 传入正常参数，获取一个 Optional 对象，并使用 orElse 方法设置默认值
        Optional optional8 = Optional.ofNullable("mydlq");
        Object object = optional8.orElse("默认值");
        System.out.println("如果值不为空："+object);

        // 传入 null 参数，获取一个 Optional 对象，并使用 orElse 方法设置默认值
        Optional optional9 = Optional.ofNullable(null);
        Object object2 = optional9.orElse("默认值");
        System.out.println("如果值为空："+object2);


        Integer asum = 0;
        Integer bsum = 0;
        Integer csum = 0;
        Integer dsum = 0;
        List<String> givenList = Arrays.asList("A","B","C","D");
        Random random = new Random();
        for (int i = 0; i <10 ; i++) {
            String randomElement = givenList.get(random.nextInt(givenList.size()));
            //System.out.println(randomElement);
            if (randomElement.equals("A")){
                asum ++;
            }else if (randomElement.equals("B")){
                bsum++;
            }else if (randomElement.equals("C")){
                csum++;
            }else if (randomElement.equals("D")){
                dsum++;
            }
        }
        System.out.println(asum);
        System.out.println(bsum);
        System.out.println(csum);
        System.out.println(dsum);


    }
}