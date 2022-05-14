package com.syraven.cloud.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-03-07 1:59 PM
 */
@Data
@Accessors(chain = true)
public class UserLog {
    private String username;
    private String userId;
    private String state;
}
