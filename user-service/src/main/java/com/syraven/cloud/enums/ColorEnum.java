package com.syraven.cloud.enums;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/11/9 11:04
 */
public enum ColorEnum {
    RED(0, "红色"),
    BLUE(1, "蓝色"),
    GREEN(2, "绿色"),

    ;

    //    可以看出这在枚举类型里定义变量和方法和在普通类里面定义方法和变量没有什么区别。唯一要注意的只是变量和方法定义必须放在所有枚举值定义的后面，否则编译器会给出一个错误。
    private int code;
    private String desc;

    ColorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 自己定义一个静态方法,通过code返回枚举常量对象
     *
     * @param code
     * @return
     */
    public static ColorEnum getValue(int code) {

        for (ColorEnum colorEnum : values()) {
            if (colorEnum.getCode() == code) {
                return colorEnum;
            }
        }
        return null;

    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}