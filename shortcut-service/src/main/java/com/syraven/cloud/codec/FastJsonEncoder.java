package com.syraven.cloud.codec;

import com.alibaba.fastjson.JSON;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import org.apache.http.entity.ContentType;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import static io.jsonwebtoken.Header.CONTENT_TYPE;

/**
 * @ClassName: FastJsonEncoder
 * @Description: 基于 FastJson 的序列化编码器
 * @Author syrobin
 * @Date 2021-12-30 2:59 PM
 * @Version V1.0
 */
public class FastJsonEncoder implements Encoder {
    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        if (object != null){
            //编码body
            template.header(CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
            template.body(JSON.toJSONBytes(object), StandardCharsets.UTF_8);
        }
    }
}
