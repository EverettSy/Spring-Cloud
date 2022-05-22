package com.syrobin.cloud.test;

import org.apiguardian.api.API;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

/**
 * * Junit5参数化测试 - Json文件来源注解<br/>
 *  * 注：需配合@ParameterizedTest + @JsonFileSource注解使用
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = EXPERIMENTAL, since = "5.0")
@ArgumentsSource(JsonArgumentsProvider.class)
public @interface JsonFileSource {

    /**
     * 参数对象对应的jsonPath（默认读取root对象）<br/>
     * jsonKey表达式示例如下：
     * <ul>
     *     <li>root对象：$</li>
     *     <li>json对象 or json数组：$.dict.addDict</li>
     *     <li>json数组中的第0个对象：$.dict.addDict[0]</li>
     * </ul>
     */
    String jsonKey() default "$";

    /**
     * json参数来源文件（*.json）（默认测试资源路径下jsonSource.json）
     */
    String resource() default "classpath:jsonSource.json";

    /**
     * json参数需要被转换的Java对象类型（默认String类型）<br/>
     * 注：复杂类型（如嵌套泛型）并未实际测试，<br/>
     * 如有特殊类型（如日期等）可默认转换成String参数后再由自定义Json工具进行反序列化
     */
    Class<?> typeClass() default String.class;
}
