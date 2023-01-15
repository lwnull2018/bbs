package com.bbs.remark.controller.message;

import com.bbs.remark.controller.AbstractController;
import com.bbs.remark.pojo.MessageTemplate;
import com.bbs.remark.response.PageDataResult;
import com.google.common.collect.Maps;
import com.bbs.remark.service.MessageTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: MessageTemplateController
 * @Description: 消息模板管理
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/20 15:17
 */
@Slf4j
@Controller
@RequestMapping("messageTemplateManage")
public class MessageTemplateController extends AbstractController {

    @Autowired
    private MessageTemplateService messageTemplateService;

    /**
     *
     * 功能描述: 跳到消息模板页面
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 13:50
     */
    @RequestMapping("/index")
    public String index() {
        return "/message/messageTemplate";
    }

    /**
     *
     * 功能描述: 分页查询消息模板列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 11:10
     */
    @RequestMapping(value = "/messageTemplateList", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getFileList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize, MessageTemplate messageTemplate) {
        /*logger.info("分页查询用户列表！搜索条件：userSearch：" + userSearch + ",pageNum:" + page.getPageNum()
                + ",每页记录数量pageSize:" + page.getPageSize());*/
        PageDataResult pdr = new PageDataResult();
        try {
            if(null == pageNum) {
                pageNum = 1;
            }
            if(null == pageSize) {
                pageSize = 10;
            }
            // 获取附件列表
            pdr = messageTemplateService.getMessageTemplateList(messageTemplate, pageNum ,pageSize);
            log.info("消息模板列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("消息模板列表查询异常！", e);
        }
        return pdr;
    }

    /**
     *
     * 功能描述: 保存消息模板
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/22 11:59
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(MessageTemplate messageTemplate, HttpServletRequest request) {

        System.out.println("messageTemplate = " + messageTemplate);

        String[] contentTypes = request.getParameterValues("contentType");
        String[] textContents = request.getParameterValues("textContent");
        String[] fileContents = request.getParameterValues("fileContent");

        System.out.println("contentTypes = " + contentTypes + " , textContents = " + textContents + " , fileContents = " + fileContents);

        JSONArray array = new JSONArray();
        for (int i=0, size=contentTypes.length; i<size; i++) {
            JSONObject json = new JSONObject();
            json.put("contentType", contentTypes[i]);
            json.put("textContent", textContents[i]);
            json.put("fileContent", fileContents[i]);

            array.put(json);
        }

        messageTemplate.setTemplateContent(array.toString());

        Map<String, Object> data = new HashMap<>();
        int result = 0;
        if (messageTemplate.getId() > 0) {
            result = messageTemplateService.updateMessageTemplate(messageTemplate);
        } else {
            result = messageTemplateService.insert(messageTemplate);
        }

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
     * 功能描述: 修改文件名
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/22 11:59
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> update(MessageTemplate messageTemplate, HttpServletRequest request) {

        System.out.println("messageTemplate = " + messageTemplate);

        int result = messageTemplateService.updateMessageTemplate(messageTemplate);

        Map<String, Object> data = new HashMap<>();
        if (result > 0) {
            data.put("code", 1);
            data.put("msg", "修改成功");
        } else {
            data.put("code", 0);
            data.put("msg", "修改失败");
        }

        return data;
    }

    /**
     *
     * 功能描述: 使用消息模板发送消息
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/22 11:59
     */
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> send(@RequestParam("id") Long id) {

        Map<String, Object> data = Maps.newHashMap();

        MessageTemplate exist = messageTemplateService.getById(id);
        if (null == id || id <= 0) {
            data.put("code", -1);
            data.put("msg", "消息模板不存在");
            log.warn("消息模板不存在");

            return data;
        }

        data = messageTemplateService.sendMessageTemplate(id);

        return data;
    }

    /**
     *
     * 功能描述: 使用消息模板发送消息
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/22 11:59
     */
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> sendMessage(HttpServletRequest request) {

        String templateIdStr = request.getParameter("templateId");
        String botIdStr = request.getParameter("botId");

        log.info("templateId:{}, botId:{}", templateIdStr, botIdStr);

        Map<String, Object> data = Maps.newHashMap();

        Long templateId = Long.parseLong(templateIdStr);
        Long botId = Long.parseLong(botIdStr);

        if (null == templateId || templateId <= 0) {
            data.put("code", -1);
            data.put("msg", "消息模板不存在");
            log.warn("消息模板不存在");

            return data;
        }

        data = messageTemplateService.sendMessageTemplate(templateId, botId);

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
        int result = messageTemplateService.delMessageTemplate(id);

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
