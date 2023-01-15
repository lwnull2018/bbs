package com.bbs.remark.service.impl;

import com.bbs.remark.pojo.enumeration.AppTypeEnum;
import com.bbs.remark.pojo.enumeration.SendContentTypeEnum;
import com.bbs.remark.pojo.CustomerMessage;
import com.bbs.remark.service.CustomerMessageService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.request.SendVideo;
import com.pengrad.telegrambot.request.SendVoice;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CustomerMessageServiceImpl  implements CustomerMessageService {

    @Value("${telegram.bot.username}")
    private String username;

    @Value("${telegram.bot.token}")
    private String token;

    @Override
    public Map<String, Object> sendMessage(CustomerMessage message) {
        Map<String, Object> data = new HashMap<>();

        AppTypeEnum appTypeEnum = AppTypeEnum.valueOf(message.getAppType());
        switch(appTypeEnum) {
            case Telegram:
                data = telegramSend(message);
                break;
            case Skype:
                break;
            case WhatsApp:
                break;
            default:
                break;
        }

        return data;
    }

    /**
     * 通过飞机发送
     * @param message
     * @return
     */
    private Map<String, Object> telegramSend(CustomerMessage message) {
        Map<String, Object> data = new HashMap<>();

        TelegramBot bot = new TelegramBot(token);

        SendContentTypeEnum sendContentTypeEnum = SendContentTypeEnum.valueOf(message.getContentType());
        SendResponse response = null;
        switch(sendContentTypeEnum) {
            case Text:
                response = bot.execute(new SendMessage(message.getChatId(), message.getTextContent()));
                break;
            case Photo:
                response = bot.execute(new SendPhoto(message.getChatId(), message.getFileContentBytes()));
                break;
            case Voice:
                response = bot.execute(new SendVoice(message.getChatId(), message.getFileContentBytes()));
                break;
            case Video:
                response = bot.execute(new SendVideo(message.getChatId(), message.getFileContentBytes()));
                break;
            default:
                log.error("发送类型错误{}", message.getContentType());
                data.put("code", -1);
                data.put("msg", "发送类型错误");
                return data;
        }
        System.out.println("response = " + response.toString());

        data.put("code", 0);
        data.put("msg", "发送成功");

        return data;
    }

}
