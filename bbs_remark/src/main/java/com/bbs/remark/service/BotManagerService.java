package com.bbs.remark.service;

import com.bbs.remark.pojo.BotManager;
import com.bbs.remark.response.PageDataResult;

import java.util.List;

public interface BotManagerService {

    int insert(BotManager botManager);

    PageDataResult getList(BotManager botManager, Integer pageNum, Integer pageSize);

    BotManager getById(Integer id);

    int save(BotManager botManager);

    int del(Integer id);

    List<BotManager> avaiableList();
}
