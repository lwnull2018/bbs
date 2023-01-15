package com.bbs.remark.service.impl;

import com.bbs.remark.dao.AttachmentFileMapper;
import com.bbs.remark.dto.AttachmentFileSearchDTO;
import com.bbs.remark.pojo.AttachmentFile;
import com.bbs.remark.response.PageDataResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bbs.remark.service.AttachmentFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AttachmentFileServiceImpl implements AttachmentFileService {

    @Autowired
    private AttachmentFileMapper attachmentFileMapper;

    @Override
    public int insert(AttachmentFile attachmentFile) {
        return attachmentFileMapper.insert(attachmentFile);
    }

    @Override
    public PageDataResult getFileList(AttachmentFileSearchDTO fileSearchDTO, Integer pageNum, Integer pageSize) {
        PageDataResult pageDataResult = new PageDataResult();
        List<AttachmentFileSearchDTO> fileMapperFileList = attachmentFileMapper.getFileList(fileSearchDTO);

        PageHelper.startPage(pageNum, pageSize);

        if(fileMapperFileList.size() != 0){
            PageInfo<AttachmentFileSearchDTO> pageInfo = new PageInfo<>(fileMapperFileList);
            pageDataResult.setList(fileMapperFileList);
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }

        return pageDataResult;
    }

    @Override
    public int updateFileName(AttachmentFile file) {
        return attachmentFileMapper.updateFileName(file);
    }

    @Override
    public List<AttachmentFile> getFiles() {
        return attachmentFileMapper.getFiles();
    }

    @Override
    public int delFile(Integer id) {
        return attachmentFileMapper.delFile(id);
    }
}
