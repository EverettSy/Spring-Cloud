package com.syraven.cloud.rocketmq;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.syraven.cloud.config.RocketMqConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author syrobin
 * @version v1.0
 * @description: 消费者客户端
 * @date 2022-09-24 21:49
 */
//@Configuration
public class ConsumerClient {

    @Autowired
    private RocketMqConfig rocketMqConfig;

    @Autowired
    private NormalMessageListener messageListener;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean buildConsumer() {
        ConsumerBean consumerBean = new ConsumerBean();
        //配置文件
        Properties properties = rocketMqConfig.getMqProperties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, rocketMqConfig.getGroupIdTest());
        //设置消费线程数固定为20个 20个为默认值
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, "20");
        consumerBean.setProperties(properties);
        //订阅关系
        Map<Subscription, MessageListener> subscriptionTable = new HashMap<>(16);

        //订阅普通消息
        Subscription subscription = new Subscription();
        subscription.setTopic(rocketMqConfig.getNormalMessageTopic());
        subscription.setExpression("Tag_SMS");
        subscriptionTable.put(subscription, messageListener);

        //订阅多个topic如上设置
        consumerBean.setSubscriptionTable(subscriptionTable);
        return consumerBean;

    }

}
