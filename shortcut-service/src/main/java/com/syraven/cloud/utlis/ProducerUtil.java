package com.syraven.cloud.utlis;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.syraven.cloud.rocketmq.ProducerClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author syrobin
 * @version v1.0
 * @description: rocketmq生产者工具类
 * @date 2022-09-25 11:12
 */
//@Component
public class ProducerUtil {

    @Autowired
    private ProducerClient producerClient;

    /**
     * 同步发送消息
     * @param msgTag 消息标签，可用于消息分类标注
     * @param topicName topic名称
     * @param msgKey 消息key，可用于消息去重，建议设置全局唯一，可不传不影响消息投递
     * @param messageBody 消息body内容，生产者自定义内容
     * @return success：SendResult or error：null
     */
    public SendResult sendMag(String msgTag, String topicName, String msgKey,byte[] messageBody) {
        Message msg = new Message(topicName, msgTag,msgKey, messageBody);
        return producerClient.buildProducer().send(msg);
    }

    /**
     * 同步发送定时/延时消息
     * @param msgTag 消息标签，可用于消息分类标注，对消息进行再归类
     * @param topicName topic名称
     * @param msgKey 消息key，可用于消息去重，建议设置全局唯一，可不传不影响消息投递
     * @param messageBody 消息body内容，生产者自定义内容,二进制形式的数据
     * @param delayTime  延时消息，在指定延迟时间（当前时间之后）进行投递。最大可设置延迟40天投递，单位毫秒（ms）。
     * @return success：SendResult or error：null
     */
    public SendResult sendTimeMsg(String msgTag, String topicName, String msgKey,byte[] messageBody,long delayTime) {
        Message msg = new Message(topicName, msgTag,msgKey, messageBody);
        msg.setStartDeliverTime(delayTime);
        return producerClient.buildProducer().send(msg);
    }

    /**
     * 单向发送消息
     * @param msgTag 消息标签，可用于消息分类标注
     * @param topicName topic名称
     * @param msgKey 消息key，可用于消息去重，建议设置全局唯一，可不传不影响消息投递
     * @param messageBody 消息body内容，生产者自定义内容
     * @return success：SendResult or error：null
     */
    public SendResult sendOneWayMsg(String msgTag, String topicName, String msgKey,byte[] messageBody) {
        Message msg = new Message(topicName, msgTag,msgKey, messageBody);
        producerClient.buildProducer().sendOneway(msg);
        return null;
    }


    /**
     * 异步发送消息
     * @param msgTag 消息标签，可用于消息分类标注
     * @param topicName topic名称
     * @param msgKey 消息key，可用于消息去重，建议设置全局唯一，可不传不影响消息投递
     * @param messageBody 消息body内容，生产者自定义内容
     * @return success：SendResult or error：null
     */
    public SendResult sendAsyncMsg(String msgTag, String topicName, String msgKey,byte[] messageBody) {
        Message msg = new Message(topicName, msgTag,msgKey, messageBody);
        producerClient.buildProducer().sendAsync(msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                // 消息发送成功
                System.out.println("send message success. topic="+sendResult.getTopic()+", msgId="+sendResult.getMessageId());
            }
            @Override
            public void onException(OnExceptionContext context) {
                // 消息发送失败,需要进行重试处理，可重新发送这条消息或者将持久化这条数据进行补偿处理
                System.out.println("send message failed. topic="+context.getTopic()+", msgId="+context.getMessageId());
            }
        });

        return null;
    }




}
