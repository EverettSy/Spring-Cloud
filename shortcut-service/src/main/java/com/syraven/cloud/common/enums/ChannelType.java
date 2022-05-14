package com.syraven.cloud.common.enums;

import com.syraven.cloud.common.dto.DingDingContentModel;
import com.syraven.cloud.common.dto.EmailContentModel;
import com.syraven.cloud.common.dto.EnterpriseWeChatContentModel;
import com.syraven.cloud.common.dto.ImContentModel;
import com.syraven.cloud.common.dto.MiniProgramContentModel;
import com.syraven.cloud.common.dto.OfficialAccountsContentModel;
import com.syraven.cloud.common.dto.PushContentModel;
import com.syraven.cloud.common.dto.SmsContentModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author syrobin
 * @version v1.0
 * @description: 渠道类型枚举
 * @date 2022-04-11 13:43
 */
@Getter
@ToString
@AllArgsConstructor
public enum ChannelType {


    IM(10, "IM(站内信)", ImContentModel.class, "im"),
    PUSH(20, "push(通知栏)", PushContentModel.class, "push"),
    SMS(30, "sms(短信)", SmsContentModel.class, "sms"),
    EMAIL(40, "email(邮件)", EmailContentModel.class, "email"),
    OFFICIAL_ACCOUNT(50, "OfficialAccounts(服务号)", OfficialAccountsContentModel.class, "official_accounts"),
    MINI_PROGRAM(60, "miniProgram(小程序)", MiniProgramContentModel.class, "mini_program"),
    ENTERPRISE_WE_CHAT(70, "EnterpriseWeChat(企业微信)", EnterpriseWeChatContentModel.class, "enterprise_we_chat"),
    DING_DING_ROBOT(80, "dingDingRobot(钉钉机器人)", DingDingContentModel.class, "ding_ding_robot"),
    DING_DING_WORK_NOTICE(90, "dingDingWorkNotice(钉钉工作通知)", DingDingContentModel.class, "ding_ding_work_notice"),
    ;

    /**
     * 编码值
     */
    private Integer code;

    /**
     * 描述
     */
    private String description;

    /**
     * 内容模型Class
     */
    private Class contentModelClass;

    /**
     * 英文标识
     */
    private String codeEn;

    /**
     * 通过code获取class
     * @param code
     * @return
     */
    public static Class getChanelModelClassByCode(Integer code) {
        ChannelType[] values = values();
        for (ChannelType value : values) {
            if (value.getCode().equals(code)) {
                return value.getContentModelClass();
            }
        }
        return null;
    }

    /**
     * 通过code获取enum
     * @param code
     * @return
     */
    public static ChannelType getEnumByCode(Integer code) {
        ChannelType[] values = values();
        for (ChannelType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
