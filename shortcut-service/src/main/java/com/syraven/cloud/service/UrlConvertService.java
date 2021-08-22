package com.syraven.cloud.service;

/**
 * @ClassName UrlConvertService
 * @Description: 短链服务service接口
 * @Author syrobin
 * @Date 2021-08-11 4:10 下午
 * @Version V1.0
 **/
public interface UrlConvertService {

    /**
     * 得到短地址URL
     *
     * @param url
     * @return
     */
    String convertUrl(String url);

    /**
     * 将短地址URL 转换为正常的地址
     *
     * @param shortUrl
     * @return
     */
    String revertUrl(String shortUrl);
}
