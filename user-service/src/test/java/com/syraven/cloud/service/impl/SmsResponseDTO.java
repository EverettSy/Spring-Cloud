package com.syraven.cloud.service.impl;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author SyRAVEN
 * @since 2021-06-02 19:51
 */
@Data
@EqualsAndHashCode
@ApiModel(value="短信接口响应DTO对象", description="短信接口响应DTO对象")
public class SmsResponseDTO {

    private Boolean first;
    private String second;
}
