package com.syrobin.cloud;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @ClassName: OssProperties
 * @Description: aws配置信息
 * 配置文件添加： oss: enable: true endpoint: http://127.0.0.1:9000 #
 * pathStyleAccess 采用nginx反向代理或者AWS S3 配置成true，支持第三方云存储配置成false pathStyleAccess: false
 * access-key: syrobin secret-key: syrobin bucket-name: syrobin region: custom-domain:
 * https://oss.xxx.com/lengleng
 * <p>
 * bucket 设置公共读权限
 * @Author syrobin
 * @Date 2021-09-15 2:05 下午
 * @Version V1.0
 */
@Data
@ConfigurationProperties(prefix = "oss")
@EnableConfigurationProperties(OssProperties.class)
public class OssProperties {

    /**
     * 对象存储服务的URL
     */
    private String endpoint;

    /**
     * 自定义域名
     */
    private String customDomain;

    /**
     * true path-style nginx 反向代理和S3默认支持 pathStyle {http://endpoint/bucketname} false
     * supports virtual-hosted-style 阿里云等需要配置为 virtual-hosted-style
     * 模式{http://bucketname.endpoint}
     */
    private Boolean pathStyleAccess;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 区域
     */
    private String region;

    /**
     * Access key就像用户ID，可以唯一标识你的账户
     */
    private String accessKey;

    /**
     * Secret key是你账户的密码
     */
    private String secretKey;

    /**
     * 默认的存储桶名称
     */
    private String bucketName = "syrboin-erp";

    /**
     * 最大线程数，默认： 100
     */
    private Integer maxConnections = 100;

}
