package com.bbs.remark.controller.message;

import com.bbs.remark.dto.UserSearchDTO;
import com.bbs.remark.pojo.CustomerMessage;
import com.google.common.collect.Maps;
import com.bbs.remark.pojo.BaseAdminUser;
import com.bbs.remark.response.PageDataResult;
import com.bbs.remark.service.AdminUserService;
import com.bbs.remark.service.CustomerMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Title: MessageController
 * @Description: 消息管理
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/20 15:17
 */
@Controller
@RequestMapping("msgManage")
public class MessageController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private CustomerMessageService customerMessageService;

    @Value("${telegram.bot.username}")
    private String username;

    @Value("${telegram.bot.token}")
    private String token;

    /**
     *
     * 功能描述: 跳到消息发送页面
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 13:50
     */
    @RequestMapping("/sendMsg")
    public String sendMsg() {
        return "/message/sendMsg";
    }

    /**
     *
     * 功能描述: 跳到消息多文件发送页面
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 13:50
     */
    @RequestMapping("/multiFileSend")
    public String multiFileSend() {
        return "/message/multiFileSend";
    }

    /**
     *
     * 功能描述: 跳到系统用户列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 13:50
     */
    @RequestMapping("/userManage")
    public String userManage() {
        return "/user/userManage";
    }

    /**
     *
     * 功能描述: 分页查询用户列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 11:10
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getUserList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,/*@Valid PageRequest page,*/ UserSearchDTO userSearch) {
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
            // 获取用户列表
            pdr = adminUserService.getUserList(userSearch, pageNum ,pageSize);
            logger.info("用户列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户列表查询异常！", e);
        }
        return pdr;
    }


    /**
     *
     * 功能描述: 新增和更新系统用户
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/22 10:14
     */
    @RequestMapping(value = "/setUser", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> setUser(BaseAdminUser user) {
        logger.info("设置用户[新增或更新]！user:" + user);
        Map<String,Object> data = new HashMap();
        if(user.getId() == null){
            data = adminUserService.addUser(user);
        }else{
            data = adminUserService.updateUser(user);
        }
        return data;
    }


    /**
     *
     * 功能描述: 发送消息
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/22 11:59
     */
    /*@RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> send(@RequestParam("chatId") Integer chatId,@RequestParam("content") String content) {
        logger.info("发送消息！chatId:" + chatId + " content:" + content);

        TelegramBot bot = new TelegramBot(token);
        SendResponse response = bot.execute(new SendMessage(chatId, content));
        System.out.println("response = " + response.toString());

        Map<String, Object> data = new HashMap<>();
        data.put("code", 0);
        data.put("msg", "发送成功");

        return data;
    }*/

    /**
     *
     * 功能描述: 发送消息
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/22 11:59
     */
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> sendContent(CustomerMessage message, HttpServletRequest request) {
        logger.info("sendContent 发送消息！chatId:" + message.getChatId() + " content:" + message.getTextContent());
        System.out.println(message);

        Map<String, Object> data = customerMessageService.sendMessage(message);

//        TelegramBot bot = new TelegramBot(token);
//        SendResponse response = bot.execute(new SendMessage(chatId, content));
//        System.out.println("response = " + response.toString());

//        Map<String, Object> data = new HashMap<>();
//        data.put("code", 0);
//        data.put("msg", "发送成功");

        return data;
    }

    @RequestMapping(value = "/uploadFile" , method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Map<String, Object> upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request)throws IOException {
        Map<String, Object> result = Maps.newHashMap();

        if (null == file) {
            System.out.println("未上传文件。。。。。。");
        }

        String chatId = request.getParameter("chatId");
        String contentType = request.getParameter("contentType");
        String appType = request.getParameter("appType");
        String textContent = request.getParameter("textContent");

        System.out.println(String.format("chatId=%s,contentType=%s,appType=%s,textContent=%s", chatId, contentType, appType, textContent));

        CustomerMessage customerMessage = new CustomerMessage();
        customerMessage.setChatId(Long.valueOf(chatId));
        customerMessage.setContentType(contentType);
        customerMessage.setAppType(appType);
        customerMessage.setFileContent(textContent);

        System.out.println(customerMessage);

        if (null != file) {
            String path = "/Users/zhang/IdeaProjects/springboot-layui/src/main/resources/upload";

            //上传文件名
            String realname = file.getOriginalFilename();//文件的原本名称
            String suffix = realname.substring(realname.lastIndexOf("."));//获取文件的后缀
            String hash = Integer.toHexString(new Random().nextInt());//生成随机数作为文件名
            String filename = hash + suffix;
            System.out.println("文件原名"+realname);
            System.out.println("文件新名称"+filename);
            File filepath = new File(path, filename);
            //判断路径是否存在，没有就创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }
            //将上传文件保存到一个目标文档中（文件名是hash名，不是原本的名字）
            try {
                File saveFile = new File(path, filename);
                String saveFilePath = saveFile.getPath();
                logger.info("文件绝对路径：" + saveFilePath);

                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(saveFile));
                out.write(file.getBytes());
                out.flush();
                out.close();
                System.out.println("文件保存完毕");

                customerMessage.setFileContentBytes(file.getBytes());
            } catch (IOException e) {
                logger.error("文件上传发生异常，异常消息：" + e.getMessage());
                result.put("code", 0);
                result.put("msg", "文件上传失败，参数错误");
                return result;
            }
        }
        // 发送消息
        Map<String, Object> data = customerMessageService.sendMessage(customerMessage);

        return data;
    }

    @RequestMapping(value = "/uploadMultiFile" , method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Map<String, Object> uploadMultiFile(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request)throws IOException {
        Map<String, Object> result = Maps.newHashMap();

        if (null == file) {
            System.out.println("未上传文件。。。。。。");
        }

        String chatId = request.getParameter("chatId");
        String contentType = request.getParameter("contentType");
        String appType = request.getParameter("appType");
        String textContent = request.getParameter("textContent");

        System.out.println(String.format("chatId=%s,contentType=%s,appType=%s,textContent=%s", chatId, contentType, appType, textContent));

        CustomerMessage customerMessage = new CustomerMessage();
        customerMessage.setChatId(Long.valueOf(chatId));
        customerMessage.setContentType(contentType);
        customerMessage.setAppType(appType);
        customerMessage.setFileContent(textContent);

        System.out.println(customerMessage);

        if (null != file) {
            String path = "/Users/zhang/IdeaProjects/springboot-layui/src/main/resources/upload";

            //上传文件名
            String realname = file.getOriginalFilename();//文件的原本名称
            String suffix = realname.substring(realname.lastIndexOf("."));//获取文件的后缀
            String hash = Integer.toHexString(new Random().nextInt());//生成随机数作为文件名
            String filename = hash + suffix;
            System.out.println("文件原名"+realname);
            System.out.println("文件新名称"+filename);
            File filepath = new File(path, filename);
            //判断路径是否存在，没有就创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }
            //将上传文件保存到一个目标文档中（文件名是hash名，不是原本的名字）
            try {
                File saveFile = new File(path, filename);
                String saveFilePath = saveFile.getPath();
                logger.info("文件绝对路径：" + saveFilePath);

                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(saveFile));
                out.write(file.getBytes());
                out.flush();
                out.close();
                System.out.println("文件保存完毕");

                customerMessage.setFileContentBytes(file.getBytes());
            } catch (IOException e) {
                logger.error("文件上传发生异常，异常消息：" + e.getMessage());
                result.put("code", 0);
                result.put("msg", "文件上传失败，参数错误");
                return result;
            }
        }
        // 发送消息
        Map<String, Object> data = customerMessageService.sendMessage(customerMessage);

        return data;
    }


}
