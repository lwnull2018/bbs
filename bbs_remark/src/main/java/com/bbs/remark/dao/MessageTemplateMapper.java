package com.bbs.remark.dao;

import com.bbs.remark.pojo.MessageTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by abc@123.com ON 2020/10/20.
 */
@Mapper
@Component
public interface MessageTemplateMapper {

    int insert(MessageTemplate template);

    List<MessageTemplate> getMessageTemplateList(MessageTemplate fileSearchDTO);

    int delMessageTemplate(@Param("id") Integer id);

    int updateMessageTemplate(MessageTemplate file);

    MessageTemplate getById(long id);
}
