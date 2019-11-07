package com.syraven.cloud.domain;

import lombok.Data;

/**
 * <<用户实体类>>
 *
 * @author Raven
 * @date 2019/11/7 22:10
 */
@Data
public class User {

    private Integer id;
    private String username;
    private String password;


    public User(int i, String syraven, String s) {
    }
}