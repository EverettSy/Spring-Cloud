package com.syraven.cloud.rocketmq;

import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.syraven.cloud.config.RocketMqConfig;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author syrobin
 * @version v1.0
 * @description: 生产者客户端
 * @date 2022-09-24 21:59
 */
//@Configuration
public class ProducerClient {

    @Autowired
    private RocketMqConfig rocketMqConfig;

    //@Bean(initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean buildProducer() {
        //ProducerBean将用于Producer集成至Spring Bean中
        ProducerBean producerBean = new ProducerBean();
        producerBean.setProperties(rocketMqConfig.getMqProperties());
        return producerBean;
    }
}
