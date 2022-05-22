package com.syrobin.cloud.test;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.syrobin.cloud.execption.MsgRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.springframework.util.ResourceUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author syrobin
 * @version v1.0
 * @description: Junit5参数化测试 - Json文件来源 - 解析器<br/>
 * 注：需配合@ParameterizedTest + @JsonFileSource注解使用
 * @date 2022-04-21 15:53
 */
@Slf4j
class JsonArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<JsonFileSource> {

    /**
     * 标注在测试方法上的@JsonFileSource注解
     */
    private JsonFileSource annotation;


    @Override
    public void accept(JsonFileSource annotation) {
        this.annotation = annotation;
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return this.parseJsonParams()
                .stream()
                .map(Arguments::of);
    }

    /**
     * 解析@JsonFileSource.resource指定的Json文件，并提取Json参数列表
     *
     * @return Java参数列表
     */
    private List<Object> parseJsonParams() {

        try {
            //读取json文件
            DocumentContext documentContext = JsonPath.parse(ResourceUtils.getFile(this.annotation.resource()));
            //解析jsonKey对应的json对象
            Object jsonEle = documentContext.read(this.annotation.jsonKey());
            if (null == jsonEle) {
                return Collections.emptyList();
            }
            //转换jsonArray为对象列表
            if (jsonEle instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) jsonEle;
                AtomicInteger atomIndex = new AtomicInteger(0);
                return jsonArray.stream()
                        .map(jsonItem -> this.convertSingleJsonParam(jsonItem, documentContext, atomIndex.getAndIncrement()))
                        .collect(Collectors.toList());
            }
            //转换json对象为单个对象
            return Collections.singletonList(this.convertSingleJsonParam(jsonEle, documentContext, null));
        } catch (Throwable ex) {
            throw new MsgRuntimeException("convert jsonFileSource exception!", ex);
        }
    }

    /**
     * 解析单个Json对象为Java参数（支持解析为String和具体Java对象）
     *
     * @param jsonEle json对象
     * @param documentContext     json上下文
     * @param index   json数组中当前jsonEle的索引
     * @return Java参数对象
     */
    private Object convertSingleJsonParam(Object jsonEle, DocumentContext documentContext, Integer index) {
        //转换json对象为Java字符串
        if (this.annotation.typeClass().equals(String.class)) {
            String jsonObjStr = JsonPath.parse(jsonEle).jsonString();
            log.debug("convert jsonObjStr:{}", jsonObjStr);
            return jsonObjStr;
        }
        //转换json对象为Java对象（特殊处理jsonArray索引读取）
        String jsonKeyWithIndex = null != index ? String.format("%s[%d]", this.annotation.jsonKey(), index) : this.annotation.jsonKey();
        Object jsonObj = documentContext.read(jsonKeyWithIndex, this.annotation.typeClass());
        log.debug("convert jsonObj:{}", jsonObj);
        return jsonObj;
    }
}
