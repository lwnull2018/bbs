package com.bbs.remark.service;

import com.bbs.remark.pojo.MessageTemplate;
import com.bbs.remark.response.PageDataResult;

import java.util.Map;

public interface MessageTemplateService {

    int insert(MessageTemplate messageTemplate);

    PageDataResult getMessageTemplateList(MessageTemplate messageTemplate, Integer pageNum, Integer pageSize);

    int delMessageTemplate(Integer id);

    int updateMessageTemplate(MessageTemplate messageTemplate);

    MessageTemplate getById(Long id);

    Map<String, Object> sendMessageTemplate(Long id);

    Map<String, Object> sendMessageTemplate(Long templateId, Long botId);

}
