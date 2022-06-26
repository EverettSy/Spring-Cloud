package com.syraven.cloud.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-06-25 17:12
 */
@Data
@ApiModel(value = "销售动态VO")
public class DynamicVO {

    @ApiModelProperty(value = "手机")
    private String phone;
    @ApiModelProperty(value = "时间")
    private String time;
}
