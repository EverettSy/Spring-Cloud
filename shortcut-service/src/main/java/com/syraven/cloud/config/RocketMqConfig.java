package com.syraven.cloud.config;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import lombok.Data;

import java.util.Properties;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-09-23 15:36
 */
@Data
//@Configuration
//@ConfigurationProperties(prefix = "rocketmq")
public class RocketMqConfig {

    private String accessKey;

    private String secretKey;

    private String nameSrvAddr;

    private String normalMessageTopic;

    private String groupIdTest;

    private String timeoutMillis;

    /**
     *
     * @return
     */
    public Properties getMqProperties() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey, secretKey);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, nameSrvAddr);

        //设置发送超时时间，单位毫秒
        properties.setProperty("SendMsgTimeoutMillis", timeoutMillis);

        return properties;
    }


}
