package com.syraven.cloud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author syrobin
 * @version v1.0
 * @description: 微信应用消息/钉钉/服务号均有多种的消息类型下发
 * @date 2022-04-11 11:33
 */
@Getter
@ToString
@AllArgsConstructor
public enum SendMessageType {

    TEXT(10, "文本"),
    VOICE(20, "语音"),
    VIDEO(30, "视频"),
    NEWS(40, "图文"),
    TEXT_CARD(50, "文本卡片"),
    FILE(60, "文件"),
    MINI_PROGRAM_NOTICE(70, "小程序通知"),
    MARKDOWN(80, "markdown"),
    TEMPLATE_CARD(90, "模板卡片"),
    IMAGE(100, "图片"),
    ;

    private Integer code;
    private String description;

}
