package com.bbs.remark.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 消息模板对象
 */
@Data
public class MessageTemplate {

    private long id;

    private String beginChatId;

    private String endChatId;

    private String templateContent;

    private String updateDate;

    private String createDate;

}
