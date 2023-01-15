package com.haiyu.bot;

import cn.hutool.core.io.FileUtil;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.SendResponse;

import java.io.BufferedInputStream;
import java.io.File;

public class SendMessageDemo {

    private static String token = "1263484735:AAFPIXijbtvXiF4qqKa66XUmCA0J7kjm1Ps";

    private static Long chatId = 682328822L;
//    private static String chatId = "@xiao_mage66";

    public static void main(String[] args) {

//        sendMessage();

//        sendPhoto();

//        sendAudio();

//        sendVoice();

        sendVideo();

    }

    /**
     * 发送文本
     * @return
     */
    private static SendResponse sendMessage() {
        TelegramBot bot = new TelegramBot(token);

//        String content = "tetttre中上考试啊有人要在在有有工肚腩 <b> 粗休 </b> <i> 斜体 </i>  <u> 下划线 </u> 百度: <a href='http://www.baidu.com'> 百度连接 </a>";
        String content = "<b>bold</b>, <strong>bold</strong>\n" +
                "<i>italic</i>, <em>italic</em>\n" +
                "<u>underline</u>, <ins>underline</ins>\n" +
                "<s>strikethrough</s>, <strike>strikethrough</strike>, <del>strikethrough</del>\n" +
                "<span class=\"tg-spoiler\">spoiler</span>, <tg-spoiler>spoiler</tg-spoiler>\n" +
                "<b>bold <i>italic bold <s>italic bold strikethrough <span class=\"tg-spoiler\">italic bold strikethrough spoiler</span></s> <u>underline italic bold</u></i> bold</b>\n" +
                "<a href=\"http://www.example.com/\">inline URL</a>\n" +
                "<a href=\"tg://user?id=123456789\">inline mention of a user</a>\n" +
                "<code>inline fixed-width code</code>\n" +
                "<pre>pre-formatted fixed-width code block</pre>\n" +
                "<a href=\"tg://user?id=682328822\">@xiao_mage66</a>\n" +
                "<pre><code class=\"language-python\">pre-formatted | fixed-width | code block written | in the Python programming |  language</code></pre>";
        SendMessage sendMessage = new SendMessage(chatId, content);
        sendMessage.parseMode(ParseMode.HTML);
        //是否预览
        sendMessage.disableWebPagePreview(true);

        int offset = 100;
        int length = 200;
        MessageEntity entity = new MessageEntity(MessageEntity.Type.text_link, offset, length);
        sendMessage.entities(entity);

        SendResponse response = bot.execute(sendMessage);
        System.out.println("response = " + response.toString());
        return response;
    }

    /**
     * 发送图片
     * @return
     */
    private static SendResponse sendPhoto() {
        TelegramBot bot = new TelegramBot(token);

//        byte[] bytes = FileUtil.readBytes("/Users/zhang/Downloads/telegram.png");

//        File file = FileUtil.newFile("/Users/zhang/Downloads/telegram.png");

        SendResponse response = bot.execute(new SendPhoto(chatId, "https://static-qn.51miz.com/images/sound/viny-disc@2x.png"));
        System.out.println("response = " + response.toString());
        return response;
    }

    /**
     * 发送多媒体
     * @return
     */
    private static SendResponse sendAudio() {
        TelegramBot bot = new TelegramBot(token);
        //1.发送已存在的链接文件
//        SendResponse response = bot.execute(new SendAudio(chatId, "https://static-qn.51miz.com/images/sound/viny-disc@2x.png"));

        //2.发送语音文件 ，聊天窗口中显示的默认文件名称 file.mp3
        byte[] bytes = FileUtil.readBytes("/Users/zhang/IdeaProjects/springboot-layui/src/test/resources/audio/newMsg.mp3");

        //聊天窗口中显示的名称为实际的文件名称 newMsg.mp3
//        File file = FileUtil.newFile("/Users/zhang/IdeaProjects/springboot-layui/src/test/resources/audio/newMsg.mp3");

        SendAudio sendAudio = new SendAudio(chatId, bytes);
        sendAudio.fileName("1212-您有新的消息");//设置文件名

        SendResponse response = bot.execute(sendAudio);
        System.out.println("response = " + response.toString());
        return response;
    }

    /**
     * 发送声音
     * @return
     */
    private static SendResponse sendVoice() {
        TelegramBot bot = new TelegramBot(token);
        //1.发送已存在的链接文件
//        SendResponse response = bot.execute(new SendAudio(chatId, "https://static-qn.51miz.com/images/sound/viny-disc@2x.png"));

        //2.发送语音文件
        byte[] bytes = FileUtil.readBytes("/Users/zhang/IdeaProjects/springboot-layui/src/test/resources/audio/newMsg.mp3");

        //文件的形式
//        File file = FileUtil.newFile("/Users/zhang/IdeaProjects/springboot-layui/src/test/resources/audio/newMsg.mp3");

        SendVoice sendVoice = new SendVoice(chatId, bytes);
        sendVoice.fileName("1212-您有新的语音消息");//设置文件名

        SendResponse response = bot.execute(sendVoice);
        System.out.println("response = " + response.toString());
        return response;
    }

    /**
     * 发送视频文件
     * @return
     */
    private static SendResponse sendVideo() {
        TelegramBot bot = new TelegramBot(token);
        //2.发送语音文件
        byte[] bytes = FileUtil.readBytes("/Users/zhang/IdeaProjects/springboot-layui/src/test/resources/audio/video-1.mp4");

        //文件的形式
//        File file = FileUtil.newFile("/Users/zhang/IdeaProjects/springboot-layui/src/test/resources/audio/video-1.mp4");

        SendVideo sendVideo = new SendVideo(chatId, bytes);
        sendVideo.fileName("你一个人在这里干嘛");//设置文件名

        SendResponse response = bot.execute(sendVideo);
        System.out.println("response = " + response.toString());
        return response;
    }

}
