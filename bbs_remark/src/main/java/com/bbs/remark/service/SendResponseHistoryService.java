package com.bbs.remark.service;

import com.bbs.remark.pojo.SendResponseHistory;
import com.bbs.remark.response.PageDataResult;

public interface SendResponseHistoryService {

    int insert(SendResponseHistory sendResponse);

    PageDataResult getList(SendResponseHistory sendResponse, Integer pageNum, Integer pageSize);

    SendResponseHistory getById(Integer id);

}
