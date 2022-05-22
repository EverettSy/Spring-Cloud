package com.syrobin.cloud.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author syrobin
 * @version v1.0
 * @description: 是否枚举
 * @date 2022-04-22 14:47
 */
@Getter
@AllArgsConstructor
public enum YesOrNoEnum {

    YES(1, "是"),
    NO(2, "否");

    private final Integer code;
    private final String message;

    /**
     * 根据code转换为枚举类型
     * @param code
     * @return
     */
    public static YesOrNoEnum getByCode(Integer code) {
        for (YesOrNoEnum yesOrNoEnum : YesOrNoEnum.values()) {
            if (yesOrNoEnum.getCode().equals(code)) {
                return yesOrNoEnum;
            }
        }
        return null;
    }
}
