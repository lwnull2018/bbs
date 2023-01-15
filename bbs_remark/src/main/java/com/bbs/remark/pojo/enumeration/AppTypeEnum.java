package com.bbs.remark.pojo.enumeration;

/**
 * 应用枚举类型
 * Created by abc@123.com ON 2020/10/20.
 */
public enum AppTypeEnum {

    Telegram("Telegram", "Telegram"),
    Skype("Skype", "Skype"),
    WhatsApp("WhatsApp", "WhatsApp"),
    UNKNOW("", "未知应用类型");

    private String val;
    private String desc;

    AppTypeEnum() {

    }

    AppTypeEnum(String val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public String getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }
}
