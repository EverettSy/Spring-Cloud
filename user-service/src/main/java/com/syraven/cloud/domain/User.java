package com.syraven.cloud.domain;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * <<用户实体类>>
 *
 * @author Raven
 * @date 2019/11/7 22:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel("用户实体类")
public class User implements Serializable {

    private Integer id;
    private String username;
    private String password;
}