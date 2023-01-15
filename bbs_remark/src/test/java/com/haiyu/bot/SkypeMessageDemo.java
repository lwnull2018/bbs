package com.haiyu.bot;

import com.skype.ChatMessage;
import com.skype.Skype;
import com.skype.SkypeException;

public class SkypeMessageDemo {

    private final static String skypeId = "live:.cid.7684fa2465f396f6";

    public static void main(String[] args) throws SkypeException {
        ChatMessage chatMessage = Skype.chat(skypeId).send("hello world");
        System.out.println(chatMessage);
    }

}
