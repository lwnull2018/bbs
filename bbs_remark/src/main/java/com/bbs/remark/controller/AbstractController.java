package com.bbs.remark.controller;

import com.bbs.remark.pojo.BaseAdminUser;
import org.apache.shiro.SecurityUtils;

public abstract class AbstractController {

    /**
     * 获取当前登陆的用户信息
     * @return
     */
    public BaseAdminUser getCurrentUser() {
        BaseAdminUser user = (BaseAdminUser) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

}
