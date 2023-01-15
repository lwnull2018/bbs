package com.bbs.remark.dao;

import com.bbs.remark.pojo.BotManager;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by abc@123.com ON 2020/10/20.
 */
@Mapper
@Component
public interface BotManagerMapper {

    int insert(BotManager sendResponse);

    List<BotManager> getList(BotManager botMananger);

    int del(@Param("id") Integer id);

    BotManager getById(long id);

    int updateById(BotManager botManager);

    List<BotManager> avaiableList();
}
