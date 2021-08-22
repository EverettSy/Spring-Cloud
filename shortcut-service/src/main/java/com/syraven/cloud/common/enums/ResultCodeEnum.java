package com.syraven.cloud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName ResultCodeEnum
 * @Description: 返回码
 * @Author syrobin
 * @Date 2021-08-2021/8/10-2:57 下午
 * @Version V1.0
 **/
@Getter
@AllArgsConstructor
public enum ResultCodeEnum {

    SUCCESS(200, "SUCCESS"),

    /**
     * 其它错误，未定义错误码的错误
     **/
    ERROR(300, "ERROR"),
    ERROR_PAGE(301, ""),

    INVALID_PARAMS(101, "参数不正确"),
    PHONE_NUMBER_ERROR(601, "手机号输入有误！"),

    //权限类错误码===================================
    USER_NOT_LOGINED(401, "您还未登录，请登录后再操作"),
    USER_NOT_AUTHORIZED(403, "您没有这个操作的权限哦"),

    UNAUTHORIZED(401,"未认证（签名错误）"),
    NOT_FOUND(404,"接口不存在"),
    INTERNAL_SERVER_ERROR(500,"服务器内部错误"),

    SEND_SMS_ERROR(602, "发送短信失败"),
    WRONG_SMS_CODE(603, "短信验证码不正确"),
    SEND_SMS_TOO_FREQ(604, "请在{0}后再次请求发送短信验证码"),


    UPLOAD_FILE_FAILED(1100, "上传文件失败"),
    UPLOAD_FILE_TYPE_INVALID(1101, "上传的文件类型不正确"),

    CAN_NOT_LOGIN_BY_WECHAT(1200, "微信自动登录失败"),
    BIND_THIRD_PARTY_USER_INFO_FAILED_4_CONFIG_NONE(1201, "绑定用户信息失败"),
    CAN_NOT_LOGIN_BY_WECHAT_UNBINDED(1202, "微信用户未绑定信息，自动登录失败"),
    BIND_THIRD_PARTY_USER_INFO_FAILED_4_LIMIT(1203, "绑定用户信息次数超限"),
    THIRD_PART_USER_APP_INFO_NOT_FOUND(1204, "未找到用户三方绑定信息"),

    LOGIN_MUTEX_LOCK_FAILED(998, "正在处理请求中，请稍后再试"),
    SYSTEM_ERROR(999, "系统繁忙，请稍后再试"),

    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID(10001, "参数无效"),
    PARAM_IS_BLANK(10002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
    PARAM_NOT_COMPLETE(10004, "参数缺失"),
    PARAM_EX(400,"参数解析异常"),


    /* 用户错误：20001-29999*/

    USER_NOT_LOGGED_IN(20001, "用户未登录"),
    USER_LOGIN_ERROR(20002, "账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(20003, "账号已被禁用"),
    USER_NOT_EXIST(20004, "用户不存在"),
    USER_HAS_EXISTED(20005, "用户已存在"),
    USER_INSERT_ERROR(20006, "用户更新失败"),
    USER_PGHOTO_CODE_ERROR(20007, "验证码错误"),
    USER_PASSWORD_CODE_ERROR(20008, "安全密码不正确"),
    USER_SEMAIL_ERROR(20009, "请先激活您的安全邮箱"),
    USER_EMAIL_ERROR(20010, "邮箱格式错误"),
    USER_EMAIL_OUT_ERROR(20011, "链接已过期"),
    USER_EMAIL_USE_ERROR(20012, "验证邮件已被使用"),
    USER_MINER_WALLET_ERROR(20013, "该钱包地址已被使用，不能删除"),
    USER_MINER_POOL_ERROR(20014, "该矿池地址已被使用，不能删除"),
    USER_MINER_TACTICS_ERROR(20015, "默认配置不能删除"),
    USER_MINER_DEFAULT_TACTICS_ERROR(20016, "该用户无默认配置"),
    USER_PASSWORD_ERROR(20017, "密码错误"),
    USER_MINER_POOL_REPEAT_ERROR(20018, "当前钱包地址已存在，请重新输入"),
    USER_MINER_POOL_ADDRESS_REPEAT_ERROR(20019, "当前矿池地址已存在，请重新输入"),
    USER_USE_MINER_POOL_ERROR(20020, "该矿池地址已被使用，不能编辑"),
    USER_USE_MINER_WALLET_ERROR(20021, "该钱包地址已被使用，不能编辑"),
    USER_MINER_POOL_ETH_REPEAT_ERROR(20022, "该钱包地址已被其他用户使用，请重新输入"),
    USER_SHA_MINER_POOL_ADDRESS_REPEAT_ERROR(20023, "当前用户名已存在，请重新输入"),
    USER_MINER_ERROR(20024, "矿机ID不存在"),
    USER_MINER_STOP_ERROR(20025, "该矿机已掉线，请稍后再试"),
    USER_FALL_BLACK_LIST(20026, "用户账户被冻结，请联系客服MM"),
    USER_WECHAT_UNCORRELATED(20027, "该微信尚未关联系统用户！"),
    USER_RE_LOGIN(20028, "用户已重新登录"),
    USER_WECHAT_CODE_INVALID(20029, "无效微信登陆code，请重新扫码"),
    USER_MANY_ACCOUNT(20032, "当前用户存在多个账户，请先选择归属公司！"),


    /* 业务错误：30001-39999 */
    SPECIFIED_QUESTIONED_USER_NOT_EXIST(30001, "某业务出现问题"),
    INVENTORY_NOT_ENOUGH(30002, "货品库存不足"),
    INVENTORY_NOT_EXIT(30003, "货品库存不存在"),
    ACTIVITY_INVENTORY_NOT_ENOUGH(30004, "活动货品库存不足"),
    ACTIVITY_INVENTORY_NOT_EXIT(30005, "活动货品库存不存在"),
    BRAND_NAME_EXIT(30006, "品牌名已存在"),

    /* 系统错误：40001-49999 */
    SYSTEM_INNER_ERROR(40001, "遇到一些小问题，请稍后再试"),

    /* 数据错误：50001-599999 */
    RESULE_DATA_NONE(50001, "数据未找到"),
    DATA_IS_WRONG(50002, "数据有误"),
    DATA_ALREADY_EXISTED(50003, "数据已存在"),

    SESSION_IS_OFFLINE(50004, "当前设备不存在或已掉线"),
    UUID_IS_ALREADY_EXIT(50005, "当前uuid已被使用，请修改uuid"),
    MAC_IS_ALREADY_EXIT(50006, "当前MAC地址已被使用，请修改mac"),

    IP_IS_ALREADY_EXIT(50007, "当前ip地址已被使用，请修改ip"),
    GROUP_ALREADY_EXIT(50008, "当前组名已被使用，请修改组名"),
    GROUP_MEMBER_ALREADY_EXIT(50009, "当前组内已存在该矿机"),


    /* 接口错误：60001-69999 */
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
    INTERFACE_OUTTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(60006, "接口负载过高"),

    /* 权限错误：70001-79999 */
    PERMISSION_NO_ACCESS(70001, "无访问权限"),

    APP_VERSION_ERROR(70002, "矿机卫士版本异常"),

    /* 手机验证码错误*/
    PHONE_FORMAT_ERROR(80001, "手机格式错误"),
    PHONE_SMS_SEND_ERROR(80002, "短信发送失败"),
    PHONE_SMS_OUT_ERROR(80003, "验证码失效"),
    PHONE_REGISTER_OUT_ERROR(80004, "手机号已被注册"),
    PHONE_REGISTER_ERROR(80005, "手机号未注册"),
    PHONE_CODE_ERROR(80006, "验证码输入错误，请重新输入"),

    LOGIN_OTHER_CITY(80007, "异地登录，请输入手机验证码"),

    FAIL(90001, "手机已注册");

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 消息内容
     */
    private String msg;

}
