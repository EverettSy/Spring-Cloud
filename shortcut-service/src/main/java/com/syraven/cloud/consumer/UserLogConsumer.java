package com.syraven.cloud.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-03-07 2:20 PM
 */
@Component
@Slf4j
public class UserLogConsumer {

    /*@KafkaListener(topics = {"sycloud"}, groupId = "sycloudGroup1")
    public void consumer(ConsumerRecord<?, ?> consumerRecord) {

        Optional<?> kafkaMessage = Optional.ofNullable(consumerRecord.value());
        log.info(">>>>>>>>>> record =" + kafkaMessage);
        if (kafkaMessage.isPresent()){
            //得到Optional实例中的值
            Object message = kafkaMessage.get();
            System.err.println("消费消息"+message);
        }
    }*/
}
