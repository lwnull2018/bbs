package com.bbs.remark.controller.message;

import com.bbs.remark.controller.AbstractController;
import com.bbs.remark.response.PageDataResult;
import com.bbs.remark.service.SendResponseHistoryService;
import com.bbs.remark.pojo.SendResponseHistory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Title: SendResponseHistoryController
 * @Description: 消息发送历史管理
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/20 15:17
 */
@Slf4j
@Controller
@RequestMapping("sendResponseHistory")
public class SendResponseHistoryController extends AbstractController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SendResponseHistoryService sendResponseHistoryService;

    /**
     *
     * 功能描述: 跳到附件页面
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 13:50
     */
    @RequestMapping("/index")
    public String index() {
        return "/message/sendResponseHistory";
    }

    /**
     *
     * 功能描述: 分页查询消息发送历史
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 11:10
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getFileList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize, SendResponseHistory sendResponseHistory) {
        PageDataResult pdr = new PageDataResult();
        try {
            if(null == pageNum) {
                pageNum = 1;
            }
            if(null == pageSize) {
                pageSize = 10;
            }
            // 获取附件列表
            pdr = sendResponseHistoryService.getList(sendResponseHistory, pageNum ,pageSize);
            logger.info("消息发送历史查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("消息发送历史查询异常！", e);
        }
        return pdr;
    }


}
