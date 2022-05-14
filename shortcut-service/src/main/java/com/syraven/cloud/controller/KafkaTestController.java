/*
package com.syraven.cloud.controller;

import com.syraven.cloud.producer.UserLogProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

*/
/**
 * @author syrobin
 * @version v1.0
 * @description: kafka 控制器
 * @date 2022-03-07 2:28 PM
 *//*

@RestController
public class KafkaTestController {

    @Autowired
    private UserLogProducer userLogProducer;

    @PostMapping("/kafka/insert")
    public String insert(String userId){
        userLogProducer.sendLog(userId);
        return null;
    }
}
*/
