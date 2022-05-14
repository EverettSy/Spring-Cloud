package com.syraven.cloud.producer;

import com.syraven.cloud.domain.UserLog;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author syrobin
 * @version v1.0
 * @description: 生产者 topicName 为 sycloud
 * @date 2022-03-07 2:01 PM
 */
//@Component
public class UserLogProducer {

    @Autowired
    //private KafkaTemplate kafkaTemplate;

    public void sendLog(String userId){
        UserLog userLog = new UserLog();
        userLog.setUsername("jhp").setUserId(userId).setState("0");
        System.err.println("发送用户日志数据："+ userLog);
        //kafkaTemplate.send("sycloud", JSONUtil.toJsonStr(userLog));
    }
}
