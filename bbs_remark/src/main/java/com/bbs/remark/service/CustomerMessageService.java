package com.bbs.remark.service;

import com.bbs.remark.pojo.CustomerMessage;

import java.util.Map;

public interface CustomerMessageService {

    Map<String,Object> sendMessage(CustomerMessage message);

}
