package com.syraven.cloud.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/5/12 17:23
 */
@Data
@ToString
@ApiModel("用户Vo实体")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserVo implements Serializable {

    public static final String Table = "t_user";

    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("年龄")
    private Integer age;
}