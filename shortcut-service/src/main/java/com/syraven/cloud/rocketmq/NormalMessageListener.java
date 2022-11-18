package com.syraven.cloud.rocketmq;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author syrobin
 * @version v1.0
 * @description: 消息监听器，用于监听消息
 * @date 2022-09-23 16:40
 */
@Component
public class NormalMessageListener implements MessageListener {

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {

        try {
            byte[] body = message.getBody();
            String msg = new String(body);
            //TODO 业务逻辑
            System.out.println("Receive: " + message);
            return Action.CommitMessage;
        } catch (Exception e) {
            //消费失败
            return Action.ReconsumeLater;
        }
    }
}
