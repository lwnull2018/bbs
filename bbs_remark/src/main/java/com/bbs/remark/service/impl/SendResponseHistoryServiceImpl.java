package com.bbs.remark.service.impl;

import cn.hutool.core.date.DateUtil;
import com.bbs.remark.dao.SendResponseHistoryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bbs.remark.pojo.SendResponseHistory;
import com.bbs.remark.response.PageDataResult;
import com.bbs.remark.service.SendResponseHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class SendResponseHistoryServiceImpl implements SendResponseHistoryService {

    @Autowired
    private SendResponseHistoryMapper sendResponseHistoryMapper;

    @Override
    public SendResponseHistory getById(Integer id) {
        return sendResponseHistoryMapper.getById(id);
    }

    @Override
    public int insert(SendResponseHistory messageTemplate) {
        messageTemplate.setCreateDate(DateUtil.formatDateTime(new Date()));
        return sendResponseHistoryMapper.insert(messageTemplate);
    }

    @Override
    public PageDataResult getList(SendResponseHistory sendResponse, Integer pageNum, Integer pageSize) {
        PageDataResult pageDataResult = new PageDataResult();
        List<SendResponseHistory> list = sendResponseHistoryMapper.getList(sendResponse);

        PageHelper.startPage(pageNum, pageSize);

        if(list.size() != 0){
            PageInfo<SendResponseHistory> pageInfo = new PageInfo<>(list);
            pageDataResult.setList(list);
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }

        return pageDataResult;
    }

}
