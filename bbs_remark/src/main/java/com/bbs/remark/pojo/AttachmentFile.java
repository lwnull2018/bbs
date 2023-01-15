package com.bbs.remark.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 附件对象
 */
@Data
public class AttachmentFile {

    private long id;

    private String fileName;

    private String originalName;

    private String filePath;

    private byte[] fileContent;

    private long createUserId;

    private Date createDate;

}
