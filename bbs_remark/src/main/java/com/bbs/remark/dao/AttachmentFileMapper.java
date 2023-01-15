package com.bbs.remark.dao;

import com.bbs.remark.dto.AttachmentFileSearchDTO;
import com.bbs.remark.pojo.AttachmentFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by abc@123.com ON 2020/10/20.
 */
@Mapper
@Component
public interface AttachmentFileMapper {

    int insert(AttachmentFile file);

    List<AttachmentFileSearchDTO> getFileList(AttachmentFileSearchDTO fileSearchDTO);

    int delFile(@Param("id") Integer id);

    int updateFileName(AttachmentFile file);

    List<AttachmentFile> getFiles();

    AttachmentFile getById(Integer id);
}
