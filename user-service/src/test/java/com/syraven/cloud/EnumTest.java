package com.syraven.cloud;

import com.syraven.cloud.domain.Book;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/11/9 11:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EnumTest {

    public static void main(String[] args) {
        /**
         * 测试枚举的values()
         *
         */
        String s = Color.getValue(0).getDesc();
        System.out.println("获取的值为:"+ s);



        /**
         * 测试枚举的valueof,里面的值可以是自己定义的枚举常量的名称
         * 其中valueOf方法会把一个String类型的名称转变成枚举项，也就是在枚举项中查找字面值和该参数相等的枚举项。
         */

        Color color =Color.valueOf("GREEN");
        System.out.println(color.getDesc());

        /**
         * 测试枚举的toString()方法
         */

        Color s2 = Color.getValue(0) ;
        System.out.println("获取的值为:"+ s2.toString());

        System.out.println(s2.name());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        LocalDateTime lastMonth = LocalDateTime.now().plus(-1, ChronoUnit.MONTHS);
        System.out.println(lastMonth.format(formatter));

        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book("as",12,"03256"));
        bookList.add(new Book("ass",126,"0325"));
        bookList.add(new Book("asds",12,"03276"));

        Map<String,String> listToMap = bookList.stream().collect(Collectors.toMap(Book::getIsbn,Book::getName));

        System.out.println(listToMap);

        DecimalFormat df2 = new DecimalFormat("###,###.##");
        System.out.println(df2.format(123456.123));

    }

    /*public Map<String,String> listToMap(List<Book> books){

        return books.stream().collect(Collectors.toMap(Book::getIsbn,Book::getName));
    }*/


}