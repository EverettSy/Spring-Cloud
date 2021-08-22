package com.syraven.cloud.utlis;

import java.util.regex.Pattern;

/**
 * @ClassName Validator
 * @Description: 验证工具
 * @Author syrobin
 * @Date 2021-08-11 4:06 下午
 * @Version V1.0
 **/
public class Validator {

    public static final String URL_REGEX = "(ftp|http|https):\\/\\/(\\w+:{0,1}\\w*@)?(\\S+)(:[0-9]+)?(\\/|\\/([\\w#!:.?+=&%@!\\-\\/])" +
            ")?";

    /**
     * 验证URL地址
     *
     * @param url
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkUrl(String url) {
        return Pattern.matches(URL_REGEX, url);
    }
}
