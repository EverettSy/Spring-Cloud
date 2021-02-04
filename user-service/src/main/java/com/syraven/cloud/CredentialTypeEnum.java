package com.syraven.cloud;

import lombok.Getter;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/11/9 11:10
 */
@Getter
public enum CredentialTypeEnum {

    ID("居民身份证");

    private String type;

    CredentialTypeEnum(String type) {
        this.type = type;
    }
}
