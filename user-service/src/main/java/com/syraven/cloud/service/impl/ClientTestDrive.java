package com.syraven.cloud.service.impl;

import com.syraven.cloud.domain.Validator;
import com.syraven.cloud.util.SM3Util;
import com.syraven.cloud.util.SM4Util;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.MapUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author SyRAVEN
 * @since 2021-04-08 08:56
 */
public class ClientTestDrive {

    public static final String demo = "SM4加解密测试DEMO";
    public static final String sortparam = "参数根据key的 ASCII 码从小到大排序（字典序）";//排序使用treemap，key1=value1&key2=value2
    public static final String appsecret = "ba22726d-14aa-11ea-9b2d-b888e3ebf769";


    public static void main(String[] args) throws Exception {
        Validator numbericValidator = new Validator((String s) -> s.matches("[a-z]+"));
        boolean res1 = numbericValidator.validate("7789");
        System.out.println(res1);

        Validator lowerCaseValidator = new Validator((String s) -> s.matches("[a-z]+"));
        boolean res2 = lowerCaseValidator.validate("aaaddd");
        System.out.println(res2);

        Map<String,Object> map = new HashMap<>(16);
        map.put("bbb", 1);
        map.put("aaa", "hello");
        map.put("abc", "abc");
        map.put("u1", "u1");
        System.out.println("排序前：" + map);
        /*map = SortUtil.sortByKey(map);
        System.out.println("排序后：" + map);*/

        //测试1
        /*String md5Value = paramsSort(map);
        System.out.println(md5Value);*/


        byte[] bKey = SM4Util.generateKey();
        byte[] sm4 = SM4Util.encrypt_Ecb_Padding(bKey,demo.getBytes(StandardCharsets.UTF_8));
        String encData = Base64.encodeBase64String(sm4);
        System.out.println("密文：" + encData);

        byte[] dd = SM4Util.decrypt_Ecb_Padding(bKey, Base64.decodeBase64(encData));
        String dataInfo = new String(dd, StandardCharsets.UTF_8);
        System.out.println("解密后的原文：" + dataInfo);

        //签名摘要
        /*md5Value+="&appsecret="+appsecret;
        byte[] signHash = SM3Util.hash(md5Value.getBytes(StandardCharsets.UTF_8));
        StringBuilder signature = new StringBuilder();
        for (byte b : signHash) {
            signature.append(byteToHexString(b));
        }
        String sign = signature.toString();
        System.out.println("签名String值为："+sign);*/

        //测试2
        String md5Value2 = createSign(map);
        System.out.println("签名String值为："+ md5Value2);

        String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        System.out.println(timestamp);
    }

    public static String byteToHexString(byte ib) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0f];
        ob[1] = Digit[ib & 0X0F];
        String str = new String(ob);
        return str;
    }



    /**
     * 报文摘要
     * @param params
     * @return 例如：a=1005&c=190010002&d=1400000001
     */
    public static String paramsSort(Map<String,Object> params){
        StringBuilder sb = new StringBuilder();
        //将参数以参数名的字段升序排序
        Map<String, Object> sortParams = new TreeMap<>(params);
        // 遍历排序的字典,并拼接"key=value"格式
        for (Map.Entry<String, Object> entry : sortParams.entrySet()) {
            String key = entry.getKey();
            Object value =  entry.getValue();
            if (Objects.nonNull(value)){
                sb.append("&").append(key).append("=").append(value);
            }
        }
        String data = sb.toString();
        data+="&appsecret="+appsecret;
        return data.replaceFirst("&","");
    }


    public static String createSign(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        List<String> paramKeys = new ArrayList<>(params.keySet());
        Collections.sort(paramKeys);
        for (String key : paramKeys) {
            sb.append("&").append(key).append("=")
                    .append(MapUtils.getString(params, key, ""));
        }
        String data = sb.toString();
        data+="&appsecret="+appsecret;
        data = data.replaceFirst("&","");

        //使用国密消息摘要算法（SM3）对报文进行摘要
        byte[] signHash = SM3Util.hash(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder signature = new StringBuilder();
        for (byte b : signHash) {
            signature.append(byteToHexString(b));
        }
        return signature.toString();
    }

    /**
     * 国密SM4加密原文
     * @param plaintext 明文
     * @return
     * @throws Exception
     */
    public static String sm4Encryption(String plaintext) throws Exception {
        byte[] key = SM4Util.generateKey();
        byte[] ciphertext = SM4Util.encrypt_Ecb_Padding(key, plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(ciphertext);
    }

    /**
     * 国密SM4解密
     * @param key 密匙
     * @param ciphertext 密文
     * @return 明文
     * @throws Exception
     */
    public static String sm4Decrypt(byte[] key, String ciphertext) throws Exception {
        byte[] plaintext = SM4Util.decrypt_Ecb_Padding(key, Base64.decodeBase64(ciphertext));
        return new String(plaintext, StandardCharsets.UTF_8);
    }
}
