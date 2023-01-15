package com.bbs.remark.service;

import com.bbs.remark.bot.SendMsgBot;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Created by abc@123.com ON 2020/10/20.
 */
@Component
public class InitializingService implements InitializingBean {


    @Autowired
    private CustomerActiveService customerActiveService;

    @Value("${telegram.bot.username}")
    private String username;

    @Value("${telegram.bot.token}")
    private String token;


    static {
//        ApiContextInitializer.init();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        DefaultBotSession session = new DefaultBotSession();
        // 实例化Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi(session.getClass());

        try {
            // 注册机器人
            botsApi.registerBot(new SendMsgBot(username, token, customerActiveService));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }



}
