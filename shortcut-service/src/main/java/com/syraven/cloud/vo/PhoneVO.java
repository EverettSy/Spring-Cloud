package com.syraven.cloud.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-06-25 17:14
 */
@Data
@ApiModel(value = "手机实体VO")
public class PhoneVO {

    @ApiModelProperty(value = "手机ID")
    private Integer id;
    @ApiModelProperty(value = "手机名称")
    private String name;
    @ApiModelProperty(value = "销量")
    private Integer sales;
}
