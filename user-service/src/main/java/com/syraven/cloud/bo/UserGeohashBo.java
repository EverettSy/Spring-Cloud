package com.syraven.cloud.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/5/8 16:55
 */
@Data
@ToString
public class UserGeohashBo {

    @ApiModelProperty(value = "距离范围/单位km")
    private Double distance;
    @ApiModelProperty(value = "geoHash的精度")
    private Integer len;
    @ApiModelProperty(value = "当前经度")
    private Double userLng;
    @ApiModelProperty(value = "当前纬度")
    private Double userLat;

}