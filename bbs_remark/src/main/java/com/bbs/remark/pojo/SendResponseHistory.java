package com.bbs.remark.pojo;

import lombok.Data;

/**
 * 消息模板对象
 */
@Data
public class SendResponseHistory {

    private Long id;

    private boolean isOk;

    private Long chatId;

    private String fromUserName;

    private String userName;

    private Long messageTemplateId;

    private String responseContent;

    private String createDate;

}
