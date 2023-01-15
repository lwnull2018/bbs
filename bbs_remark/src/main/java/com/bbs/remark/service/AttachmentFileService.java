package com.bbs.remark.service;

import com.bbs.remark.dto.AttachmentFileSearchDTO;
import com.bbs.remark.pojo.AttachmentFile;
import com.bbs.remark.response.PageDataResult;

import java.util.List;

public interface AttachmentFileService {

    int insert(AttachmentFile attachmentFile);

    PageDataResult getFileList(AttachmentFileSearchDTO fileSearchDTO, Integer pageNum, Integer pageSize);

    int delFile(Integer id);

    int updateFileName(AttachmentFile file);

    List<AttachmentFile> getFiles();
}
