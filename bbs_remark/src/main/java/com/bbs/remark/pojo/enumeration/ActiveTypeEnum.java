package com.bbs.remark.pojo.enumeration;

/**
 * 活动枚举类型
 * Created by abc@123.com ON 2020/10/20.
 */
public enum ActiveTypeEnum {

    START_WORK("START_WORK", "上班"),
    OFF_WORK("OFF_WORK", "下班"),
    SMALL_TOILET("SMALL_TOILET", "小厕所"),
    BIG_TOILET("BIG_TOILET", "大厕所"),
    EATING("EATING", "吃饭"),
    SMOKING("SMOKING", "抽烟"),
    FREE_ACTIVE("FREE_ACTIVE", "自由活动"),
    BACK("BACK", "回座"),
    VIEW("VIEW", "查看"),
    STATUS("STATUS", "状态"),
    UNKNOW("UNKNOW", "未知活动类型");

    private String activeCode;
    private String activeName;

    ActiveTypeEnum() {

    }

    ActiveTypeEnum(String code, String name) {
        this.activeCode = code;
        this.activeName = name;
    }

    public static ActiveTypeEnum nameFrom(String name) {
        for (ActiveTypeEnum activeTypeEnum : ActiveTypeEnum.values()) {
            if (activeTypeEnum.getActiveName().equalsIgnoreCase(name)) {
                return activeTypeEnum;
            }
        }
        return UNKNOW;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public String getActiveName() {
        return activeName;
    }
}
