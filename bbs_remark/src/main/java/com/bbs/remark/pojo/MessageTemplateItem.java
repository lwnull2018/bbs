package com.bbs.remark.pojo;

import lombok.Data;

@Data
public class MessageTemplateItem {
    private String contentType;
    private String textContent;
    private String fileContent;
}
