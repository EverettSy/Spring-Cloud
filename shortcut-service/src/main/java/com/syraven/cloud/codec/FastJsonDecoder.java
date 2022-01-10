package com.syraven.cloud.codec;

import com.alibaba.fastjson.JSON;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.DecodeException;
import feign.codec.Decoder;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @ClassName: FastJsonDecoder
 * @Description: 基于 FastJson 的反序列化解码器
 * @Author syrobin
 * @Date 2021-12-29 5:42 PM
 * @Version V1.0
 */
public class FastJsonDecoder implements Decoder {
    @Override
    public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
        //读取body
        byte[] body = Util.toByteArray(response.body().asInputStream());
        return JSON.parseObject(body, type);
    }
}
