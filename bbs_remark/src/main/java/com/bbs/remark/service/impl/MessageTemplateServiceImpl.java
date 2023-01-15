package com.bbs.remark.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.bbs.remark.dao.AttachmentFileMapper;
import com.bbs.remark.dao.MessageTemplateMapper;
import com.bbs.remark.dao.SendResponseHistoryMapper;
import com.bbs.remark.pojo.*;
import com.bbs.remark.pojo.enumeration.SendContentTypeEnum;
import com.bbs.remark.response.PageDataResult;
import com.bbs.remark.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.bbs.remark.dao.BotManagerMapper;
import com.bbs.remark.service.MessageTemplateService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.request.SendVideo;
import com.pengrad.telegrambot.request.SendVoice;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Autowired
    private AttachmentFileMapper attachmentFileMapper;

    @Autowired
    private SendResponseHistoryMapper sendResponseMapper;

    @Autowired
    private BotManagerMapper botManagerMapper;

    @Value("${telegram.bot.username}")
    private String username;

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public MessageTemplate getById(Long id) {
        return messageTemplateMapper.getById(id);
    }

    @Override
    public int insert(MessageTemplate messageTemplate) {
        messageTemplate.setCreateDate(DateUtil.formatDateTime(new Date()));
        return messageTemplateMapper.insert(messageTemplate);
    }

    @Override
    public PageDataResult getMessageTemplateList(MessageTemplate messageTemplate, Integer pageNum, Integer pageSize) {
        PageDataResult pageDataResult = new PageDataResult();
        List<MessageTemplate> messageTemplateList = messageTemplateMapper.getMessageTemplateList(messageTemplate);

        PageHelper.startPage(pageNum, pageSize);

        if(messageTemplateList.size() != 0){
            PageInfo<MessageTemplate> pageInfo = new PageInfo<>(messageTemplateList);
            pageDataResult.setList(messageTemplateList);
            pageDataResult.setTotals((int) pageInfo.getTotal());
        }

        return pageDataResult;
    }

    @Override
    public int delMessageTemplate(Integer id) {
        return messageTemplateMapper.delMessageTemplate(id);
    }

    @Override
    public int updateMessageTemplate(MessageTemplate messageTemplate) {
        int result = 0;
        if (messageTemplate.getId() > 0) {
            MessageTemplate exist = messageTemplateMapper.getById(messageTemplate.getId());
            if (exist != null) {
                exist.setBeginChatId(messageTemplate.getBeginChatId());
                exist.setEndChatId(messageTemplate.getEndChatId());
                exist.setTemplateContent(messageTemplate.getTemplateContent());
                exist.setUpdateDate(DateUtil.formatDateTime(new Date()));

                result = messageTemplateMapper.updateMessageTemplate(exist);
            }
        } else {
            result = messageTemplateMapper.insert(messageTemplate);
        }

        return result;
    }

    @Override
    public Map<String, Object> sendMessageTemplate(Long templateId, Long botId) {
        Map<String, Object> data = Maps.newHashMap();

        MessageTemplate exist = messageTemplateMapper.getById(templateId);

        if (null == exist) {
            data.put("code", -1);
            data.put("msg", "消息模板不存在");
            return data;
        }

        BotManager bot = botManagerMapper.getById(botId);
        if (null == bot || !bot.isStatus()) {
            data.put("code", -1);
            data.put("msg", "消息模板不存在");
            return data;
        }

        JSONArray templateContent = JSONUtil.parseArray(exist.getTemplateContent());

        Long startChatId = Long.parseLong(exist.getBeginChatId());
        Long endChatId = Long.parseLong(exist.getEndChatId());

        if (endChatId >= startChatId) {
            for (Long chatId=startChatId; chatId < endChatId + 1; chatId++) {
                for (int i=0; i<templateContent.size(); i++) {
                    MessageTemplateItem item = templateContent.get(i, MessageTemplateItem.class);

                    SendContentTypeEnum sendContentTypeEnum = SendContentTypeEnum.valueOf(item.getContentType());
                    switch (sendContentTypeEnum) {
                        case Text:
                            if (StringUtils.isBlank(item.getTextContent())) {
                                continue;
                            }
                            telegramSend(templateId, bot.getToken(), chatId, sendContentTypeEnum, item.getTextContent(), null);

                            break;
                        default:
                            if (StringUtils.isBlank(item.getFileContent())) {
                                continue;
                            }
                            int fileId = Integer.parseInt(item.getFileContent());
                            AttachmentFile attachmentFile = attachmentFileMapper.getById(fileId);

                            telegramSend(templateId, bot.getToken(), chatId, sendContentTypeEnum, null, attachmentFile.getFileContent());
                            break;
                    }
                }
            }
        }

        data.put("code", 1);
        data.put("msg", "消息发送成功");
        return data;
    }

    @Override
    public Map<String, Object> sendMessageTemplate(Long id) {
        Map<String, Object> data = Maps.newHashMap();

        MessageTemplate exist = messageTemplateMapper.getById(id);

        if (null == exist) {
            data.put("code", -1);
            data.put("msg", "消息模板不存在");
            return data;
        }

        JSONArray templateContent = JSONUtil.parseArray(exist.getTemplateContent());

        Long startChatId = Long.parseLong(exist.getBeginChatId());
        Long endChatId = Long.parseLong(exist.getEndChatId());

        if (endChatId >= startChatId) {
            for (Long chatId=startChatId; chatId < endChatId + 1; chatId++) {
                for (int i=0; i<templateContent.size(); i++) {
                    MessageTemplateItem item = templateContent.get(i, MessageTemplateItem.class);

                    SendContentTypeEnum sendContentTypeEnum = SendContentTypeEnum.valueOf(item.getContentType());
                    switch (sendContentTypeEnum) {
                        case Text:
                            if (StringUtils.isBlank(item.getTextContent())) {
                                continue;
                            }
                            telegramSend(id, token, chatId, sendContentTypeEnum, item.getTextContent(), null);

                            break;
                        default:
                            if (StringUtils.isBlank(item.getFileContent())) {
                                continue;
                            }
                            int fileId = Integer.parseInt(item.getFileContent());
                            AttachmentFile attachmentFile = attachmentFileMapper.getById(fileId);

                            telegramSend(id, token, chatId, sendContentTypeEnum, null, attachmentFile.getFileContent());
                            break;
                    }
                }
            }
        }

        data.put("code", 1);
        data.put("msg", "操作成功");
        return data;
    }

    /**
     * 通过飞机发送
     * @param templateSourceId
     * @param token
     * @param chatId
     * @param sendContentTypeEnum
     * @param textContent
     * @param fileContentBytes
     * @return
     */
    private SendResponse telegramSend(Long templateSourceId, String token, Long chatId, SendContentTypeEnum sendContentTypeEnum, String textContent, byte[] fileContentBytes) {
        TelegramBot bot = new TelegramBot(token);

        SendResponse response = null;
        switch(sendContentTypeEnum) {
            case Text:
                response = bot.execute(new SendMessage(chatId, textContent));
                break;
            case Photo:
                response = bot.execute(new SendPhoto(chatId, fileContentBytes));
                break;
            case Voice:
                response = bot.execute(new SendVoice(chatId, fileContentBytes));
                break;
            case Video:
                response = bot.execute(new SendVideo(chatId, fileContentBytes));
                break;
            default:
                log.error("发送类型错误{}", sendContentTypeEnum);
                return null;
        }

        log.info("发送响应结果：{}", response.description());

        //保存响应消息
        SendResponseHistory receiveSendResponse = new SendResponseHistory();
        receiveSendResponse.setMessageTemplateId(templateSourceId);
        receiveSendResponse.setChatId(chatId);
        receiveSendResponse.setCreateDate(DateUtil.formatDateTime(new Date()));
        if (response.isOk()) {
            receiveSendResponse.setOk(true);
            receiveSendResponse.setFromUserName(response.message().from().username());
            receiveSendResponse.setUserName(response.message().chat().username());
            receiveSendResponse.setResponseContent(response.toString());
        } else {
            receiveSendResponse.setOk(false);
            receiveSendResponse.setResponseContent(response.description());
        }

        sendResponseMapper.insert(receiveSendResponse);

        return response;
    }

}
