package com.syraven.cloud.domain;

import lombok.Data;

import java.util.List;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-05-22 13:49
 */
@Data
public class User {

    private String no;
    private String name;
    private String permissions;
    private List<String> permissionsList;
    private int state;

    private User sub;
}
