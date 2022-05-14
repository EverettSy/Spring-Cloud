package com.syraven.cloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author syrobin
 * @version v1.0
 * @description: 钉钉 自定义机器人 + 工作通知
 * <p>
 * https://open.dingtalk.com/document/group/custom-robot-access
 * <p>
 * https://open.dingtalk.com/document/orgapp-server/asynchronous-sending-of-enterprise-session-messages
 * @date 2022-04-11 13:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DingDingContentModel extends ContentModel {

    /**
     * 发送类型
     */
    private String sendType;

    /**
     * 【文本消息】需要发送的内容
     */
    private String content;

    /**
     * 图片、文件、语音消息 需要发送使用的素材ID字段
     */
    private String mediaId;
}
