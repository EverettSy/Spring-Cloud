package com.syraven.cloud;

import com.syraven.cloud.domain.UserRoot;
import com.syraven.cloud.utlis.KryoUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * @author syrobin
 * @version v1.0
 * @description: Kryo类测试
 * @date 2022-02-25 4:17 PM
 */
public class KryoTest extends SpringTest {

    @Test
    @DisplayName("Kryo")
    public void userInfo(){
        UserRoot userRoot = UserRoot.builder()
                .username("张三")
                .age(12)
                .id(12)
                .password("123456")
                .build();

        byte[] tempByteArray = KryoUtil.writeToByteArray(userRoot);
        //tempByteArray 就是序列化的结果，直接放到 Redis 里面即可
        System.out.println(tempByteArray);

        UserRoot userRoot1 = KryoUtil.readFromByteArray(tempByteArray);
        //user1 就是反序列化之后的对象
        //如果你们的存储服务不支持二进制数据（或者说不是 “二进制安全” 的），那么也可以序列化成 String：
        String tempStr = KryoUtil.writeToString(userRoot);
        System.out.println(tempStr);
        //tempStr 就是序列化的结果
        UserRoot domainA1 = KryoUtil.readFromString(tempStr);
        //domainA1 就是反序列化之后的对象
        Assert.assertEquals(userRoot,domainA1);



    }
}
