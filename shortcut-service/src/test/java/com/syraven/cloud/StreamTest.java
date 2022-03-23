package com.syraven.cloud;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

/**
 * @ClassName: StreamTest
 * @Description:
 * @Author syrobin
 * @Date 2021-11-16 5:33 下午
 * @Version V1.0
 */
public class StreamTest extends SpringTest {

    /*public static void main(String[] args) {
        List<User> users = Lists.newArrayList();
        users.add(new User(1, "张三", "123", 30, 175));
        users.add(new User(1, "红发", "123", 40, 180));
        users.add(new User(1, "白胡子", "123", 50, 185));

        //reduce reduce 操作可以实现从一组值中生成一个值
        Integer count = users.stream().map(User::getAge).reduce(0, Integer::sum);
        System.out.println(count);

        //flatMap 将多个Stream合并为一个Stream。惰性求值
        List<User> userList = Stream.of(users, Arrays.asList(
                        new User(4, "艾斯", "123", 43, 155),
                        new User(3, "雷雳", "123", 23, 176)))
                .flatMap(Collection::stream).collect(Collectors.toList());
        System.out.println(userList);

        //max和min
        Optional<User> max = users.stream().max(Comparator.comparing(User::getAge));
        Optional<User> min = users.stream().min(Comparator.comparing(User::getAge));

        //判断是否有值
        if (max.isPresent()) {
            System.out.println(max.get());
        }
        if (min.isPresent()) {
            System.out.println(min.get());
        }

        //SPI 获取bean
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ConfigA.class);
        ServiceInterface bean = ctx.getBean(ServiceInterface.class);
        bean.test();


    }*/

    @Autowired
    private RedisTemplate redisTemplate;

    public static void foo(int x, Predicate<Integer> condition) {
        for (int i = 1; i < x; i++) {
            if (condition.test(i)) {
                System.out.print(" " + i);
            }
        }
    }

    public static void main(String[] args) {

        /*Predicate<Integer> p1 = (n) -> {
            *//*while (n != 0) {
                if (n % 10 == 7)
                    return true;
               n/= 10;
            }
            return false;*//*
            if (n % 7 != 0) return false;
            if (n % 3 == 0) return true;
            return String.valueOf(n).contains("7");
        };
        foo(100, p1);*/

        /*int x = 121;
        boolean result = isPalindrome(x);
        System.out.println(result);*/
        String initialReference = "initial value referenced";
        AtomicReference<String> atomicStringReference = new AtomicReference<>(initialReference);

        String newReference = "new value referenced";
        boolean exchanged = atomicStringReference.compareAndSet(initialReference,newReference);
        System.out.println("exchanged:"+ exchanged);
        System.out.println("newReference: "+ newReference);
        System.out.println("initialReference: "+ initialReference);
        System.out.println("atomicStringReference: " +atomicStringReference.get());

        exchanged = atomicStringReference.compareAndSet(initialReference,newReference);
        System.out.println("exchanged:"+ exchanged);

        System.out.println("atomicStringReference: " +atomicStringReference.get());

        System.out.println(SystemUtils.getJavaHome());




    }


    public static boolean is(int n) {
        if (n % 7 != 0) return false;
        if (n % 3 == 0) return true;
        return String.valueOf(n).contains("7");
    }

    public static boolean isPalindrome(int x) {
        //思考：这里大家可以思考一下，为什么末尾为 0 就可以直接返回 false
        if (x < 0 || (x % 10 == 0 && x != 0)) return false;
        int revertedNumber = 0;
        while (x > revertedNumber) {
            revertedNumber = revertedNumber * 10 + x % 10;
            x /= 10;
        }
        return x == revertedNumber || x == revertedNumber / 10;
    }








}
