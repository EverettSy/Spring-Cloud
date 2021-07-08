package com.syraven.cloud;

import com.syraven.cloud.domain.User;
import org.apache.commons.lang.SerializationUtils;

public class UserServiceApplicationTests {

    private static User user2;

    public static void main(String[] args) {

        //金额的计算
        //1.变量创建
        User user = new User(); //.var
        user.setUsername("张三");
        User clone = (User) SerializationUtils.clone(user);
        System.out.println(clone);

        user2 = new User();//filed 全局变量
        User u3 = new User();


        User  user1 = (User) new Object();

        if (user1 == null) {
            if (u3 != null) {

            }

        }
        boolean flag = true;
        if (flag) {

        }
        while (flag) {
            System.out.printf("z", flag);
            System.out.println("flag = " + flag);
            //return flag;
            String [] users = new String[5];
            for (int i = 0; i < users.length; i++) {
                
            }
            for (String s : users) {
                
            }
            for (int i = users.length - 1; i >= 0; i--) {

            }
        }
        try {
            main(new String[5]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
