package com.bbs.remark.pojo;

import lombok.Data;

/**
 * 自定义消息对象
 */
@Data
public class CustomerMessage {

    private long id;

    private long chatId;

    private String appType;

    private String contentType;

    private String textContent;

    private String fileContent;

    private byte[] fileContentBytes;

}
