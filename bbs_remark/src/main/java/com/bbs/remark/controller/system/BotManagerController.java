package com.bbs.remark.controller.system;

import com.google.common.collect.Maps;
import com.bbs.remark.controller.AbstractController;
import com.bbs.remark.pojo.BotManager;
import com.bbs.remark.response.PageDataResult;
import com.bbs.remark.service.BotManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: BotManagerController
 * @Description: 机器人管理
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/20 15:17
 */
@Slf4j
@Controller
@RequestMapping("botManager")
public class BotManagerController extends AbstractController {

    @Autowired
    private BotManagerService botManagerService;

    /**
     *
     * 功能描述: 跳到列表页面
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 13:50
     */
    @RequestMapping("/index")
    public String index() {
        return "/bot/botManager";
    }

    /**
     *
     * 功能描述: 分页查询机器人列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 11:10
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize, BotManager botMananger) {
        PageDataResult pdr = new PageDataResult();
        try {
            if(null == pageNum) {
                pageNum = 1;
            }
            if(null == pageSize) {
                pageSize = 10;
            }
            // 获取附件列表
            pdr = botManagerService.getList(botMananger, pageNum ,pageSize);
            log.info("机器人列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("机器人列表查询异常！", e);
        }
        return pdr;
    }

    /**
     *
     * 功能描述: 可用机器人列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 11:10
     */
    @RequestMapping(value = "/avaiableList", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> avaiableList() {
        Map<String, Object> data = new HashMap<>();
        try {
            // 获取附件列表
            List<BotManager> list = botManagerService.avaiableList();
            log.info("可用机器人列表查询=pdr:" + list);

            data.put("code", 1);
            data.put("data", list);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("可用机器人列表查询异常！", e);
        }
        return data;
    }

    /**
     *
     * 功能描述: 保存或更新机器人
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/22 11:59
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> saveOrUpdate(BotManager botManager, HttpServletRequest request) {

        System.out.println("botManager = " + botManager);

        Map<String, Object> data = new HashMap<>();
        Long userId = getCurrentUser().getId().longValue();
        botManager.setCreateUserId(userId);
        botManager.setUpdateUserId(userId);
        int result = botManagerService.save(botManager);

        if (result > 0) {
            data.put("code", 1);
            data.put("msg", "操作成功");
        } else {
            data.put("code", 0);
            data.put("msg", "操作失败");
        }

        return data;
    }

    /**
     *
     * 功能描述: 删除消息模板
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/22 11:59
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> del(@RequestParam("id") Integer id) {
        int result = botManagerService.del(id);

        Map<String, Object> data = Maps.newHashMap();
        if (result > 0) {
            data.put("code", 1);
            data.put("msg", "消息模板删除成功");
            log.info("消息模板删除成功");
        } else {
            data.put("code", -1);
            data.put("msg", "消息模板删除失败");
            log.warn("消息模板删除失败");
        }
        return data;
    }

}
