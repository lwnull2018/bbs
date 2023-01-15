package com.bbs.remark.pojo;

import lombok.Data;

import javax.persistence.Table;

/**
 * 机器人管理对象
 */
@Data
@Table(name = "bot_manager")
public class BotManager {

    private Long id;

    private boolean status;

    private String token;

    private String username;

    private String remarks;

    private Long createUserId;

    private Long updateUserId;

    private String createDate;

    private String updateDate;

}
