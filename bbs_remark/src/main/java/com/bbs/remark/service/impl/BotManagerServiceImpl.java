package com.bbs.remark.service.impl;

import cn.hutool.core.date.DateUtil;
import com.bbs.remark.pojo.BotManager;
import com.bbs.remark.response.PageDataResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bbs.remark.dao.BotManagerMapper;
import com.bbs.remark.service.BotManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class BotManagerServiceImpl implements BotManagerService {

    @Autowired
    private BotManagerMapper botManagerMapper;

    @Override
    public BotManager getById(Integer id) {
        return botManagerMapper.getById(id);
    }

    @Override
    public int insert(BotManager botManager) {
        botManager.setCreateDate(DateUtil.formatDateTime(new Date()));
        return botManagerMapper.insert(botManager);
    }

    @Override
    public int save(BotManager botManager) {
        if (null != botManager.getId() && botManager.getId() > 0) {
            botManager.setUpdateDate(DateUtil.formatDateTime(new Date()));
            return botManagerMapper.updateById(botManager);
        } else {
            botManager.setCreateDate(DateUtil.formatDateTime(new Date()));
            return botManagerMapper.insert(botManager);
        }
    }

    @Override
    public PageDataResult getList(BotManager botManager, Integer pageNum, Integer pageSize) {
        PageDataResult pageDataResult = new PageDataResult();
        List<BotManager> list = botManagerMapper.getList(botManager);

        PageHelper.startPage(pageNum, pageSize);

        if(list.size() != 0){
            PageInfo<BotManager> pageInfo = new PageInfo<>(list);
            pageDataResult.setList(list);
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }

        return pageDataResult;
    }

    @Override
    public int del(Integer id) {
        return botManagerMapper.del(id);
    }

    @Override
    public List<BotManager> avaiableList() {
        return botManagerMapper.avaiableList();
    }
}
