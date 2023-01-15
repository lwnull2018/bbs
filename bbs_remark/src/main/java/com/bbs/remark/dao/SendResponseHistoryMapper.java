package com.bbs.remark.dao;

import com.bbs.remark.pojo.SendResponseHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by abc@123.com ON 2020/10/20.
 */
@Mapper
@Component
public interface SendResponseHistoryMapper {

    int insert(SendResponseHistory sendResponse);

    List<SendResponseHistory> getList(SendResponseHistory sendResponse);

    int delSendResponse(@Param("id") Integer id);

    SendResponseHistory getById(long id);

}
