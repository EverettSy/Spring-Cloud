package com.syraven.cloud.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author syrobin
 * @version v1.0
 * @description: 手机实体
 * @date 2022-06-25 17:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Phone", description = "手机实体")
public class Phone implements Serializable {

    private static final long serialVersionUID = 7740284161641516541L;

    @ApiModelProperty("手机id")
    private Integer id;

    @ApiModelProperty("手机名称")
    private String name;
    @ApiModelProperty("排行")
    private String ranking;

    public Phone(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
