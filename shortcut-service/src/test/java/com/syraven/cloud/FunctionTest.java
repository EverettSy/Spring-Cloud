package com.syraven.cloud;


import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author syrobin
 * @version v1.0
 * @description: 函数式编程 Test
 * @date 2022-08-04 16:08
 */
public class FunctionTest extends StreamTest {


    //public static void main(String[] args) {
    //    //中文名一般都是两个字或者三个字，现判断中文名是否不大于3个字？
    //    String username = "刘小爱";
    //    methodNegate(username, name -> name.length() > 3);
    //    //1⃣️第一个lambda表达式：判断长度是否大于3；
    //    //2⃣️第二个lambda表达式：判断是否包含“爱”字；
    //    methodAnd(username, name -> name.length() > 3, name -> name.contains("爱"));
    //    //③or()
    //    methodOr(username, name -> name.length() > 3, name -> name.contains("刘"));
    //
    //    //④apply() 将一个类型的数据转换成另一个类型的数据
    //    functionApply();
    //    //⑤andThen andThen方法，用来进行组合操作
    //    functionAndThen("123", Integer::parseInt, t -> t * 10);
    //
    //}


    private static void methodNegate(String name, Predicate<String> predicate) {
        //negate(): 取非
        boolean result = predicate.negate().test(name);
        System.out.println("名字不超过3个字: " + result);
    }

    /**
     * and() 对应逻辑运算符中的&&，也就是两者的条件都要满足才行。
     *
     * @param name
     * @param predicate1
     * @param predicate2
     */
    private static void methodAnd(String name, Predicate<String> predicate1, Predicate<String> predicate2) {
        //and(): 先推断1：不大于三个字，再推断2：包含“爱”字，最后二者并起来
        boolean result = predicate1.negate().and(predicate2).test(name);
        System.out.println("名字不超过3个字并且包含爱字: " + result);
    }

    /**
     * or() 对应逻辑运算符中的||，也就是两者中的一个满足即可。
     *
     * @param name
     * @param predicate1
     * @param predicate2
     */
    private static void methodOr(String name, Predicate<String> predicate1, Predicate<String> predicate2) {
        //and(): 先推断1：不大于三个字，再推断2：包含“爱”字，最后二者并起来
        boolean result = predicate1.negate().and(predicate2).test(name);
        System.out.println("名字不超过3个字或者包含刘字: " + result);
    }

    /**
     * apply() 将一个类型的数据转换成另一个类型的数据
     */
    private static void functionApply() {
        Function<String, Integer> function = Integer::parseInt;
        Integer integer = function.apply("123");
        System.out.println(integer);
    }

    private static void functionAndThen(String str, Function<String, Integer> function1,
                                        Function<Integer, Integer> function2) {
        //andThen(): 先执行function1，再执行function2
        Integer result = function1.andThen(function2).apply(str);
        System.out.println(result);
        Objects.requireNonNull(result);

    }

    public static Integer operator(Integer a, Integer b, BiFunction<Integer, Integer,Integer> function) {
        return function.apply(a, b);
    }


    /*public static void main(String[] args) {

        System.out.println(operator(1, 2, Integer::sum));
        System.out.println(operator(1, 2, (a, b) -> a - b));
        System.out.println(operator(1, 2, (a, b) -> a * b));
        System.out.println(operator(1, 2, (a, b) -> a / b));


    }*/

    

}


