package com.syraven.cloud.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <<用户实体类>>
 *
 * @author Raven
 * @date 2019/11/7 22:10
 */
@Builder
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("姓名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
}