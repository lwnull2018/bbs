package com.bbs.remark.pojo.enumeration;

/**
 * 发送内容枚举类型
 * Created by abc@123.com ON 2020/10/20.
 */
public enum SendContentTypeEnum {

    Text("Text", "文本"),
    Photo("Photo", "图片"),
    Voice("Voice", "语音"),
    Video("Video", "视频"),
    UNKNOW("", "未知应用类型");

    private String val;
    private String desc;

    SendContentTypeEnum() {

    }

    SendContentTypeEnum(String val, String desc) {
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
