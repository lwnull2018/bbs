package com.bbs.remark.controller.message;

import com.bbs.remark.dto.AttachmentFileSearchDTO;
import com.google.common.collect.Maps;
import com.bbs.remark.controller.AbstractController;
import com.bbs.remark.pojo.AttachmentFile;
import com.bbs.remark.response.PageDataResult;
import com.bbs.remark.service.AdminUserService;
import com.bbs.remark.service.AttachmentFileService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.*;

/**
 * @Title: AttachmentFileController
 * @Description: 附件管理
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/20 15:17
 */
@Slf4j
@Controller
@RequestMapping("attachmentFileManage")
public class AttachmentFileController extends AbstractController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private AttachmentFileService attachmentFileService;

    @Value("${upload.filePath}")
    private String uploadFilePath;

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
        return "/message/attachmentFile";
    }

    /**
     *
     * 功能描述: 分页查询附件列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 11:10
     */
    @RequestMapping(value = "/fileList", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getFileList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,/*@Valid PageRequest page,*/ AttachmentFileSearchDTO fileSearchDTO) {
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
            pdr = attachmentFileService.getFileList(fileSearchDTO, pageNum ,pageSize);
            logger.info("附件列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("附件列表查询异常！", e);
        }
        return pdr;
    }

    /**
     *
     * 功能描述: 附件列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 11:10
     */
    @RequestMapping(value = "/files", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> fileList() {
        Map<String, Object> result = Maps.newHashMap();
        List<AttachmentFile> list = null;
        try {
            // 获取附件列表
            list = attachmentFileService.getFiles();

            result.put("code", 1);
            result.put("data", list);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("附件列表获取发生异常！", e);
        }

        result.put("code", 0);
        result.put("msg", "文件列表获取失败");

        return result;
    }

    @RequestMapping(value = "/uploadFile" , method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Map<String, Object> upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request)throws IOException {
        Map<String, Object> result = Maps.newHashMap();

        if (null == file) {
            System.out.println("未上传文件。。。。。。");
            result.put("code", -1);
            result.put("msg", "未上传文件");
            return result;
        }

        String fileName = request.getParameter("fileName");

        System.out.println(String.format("fileName=%s", fileName));

        AttachmentFile attachmentFile = new AttachmentFile();
        attachmentFile.setFileName(fileName);

        System.out.println(attachmentFile);

        if (null != file) {
            //上传文件名
            String realname = file.getOriginalFilename();//文件的原本名称
            String suffix = realname.substring(realname.lastIndexOf("."));//获取文件的后缀
            String hash = Integer.toHexString(new Random().nextInt());//生成随机数作为文件名
            String filename = hash + suffix;
            System.out.println("文件原名"+realname);
            System.out.println("文件新名称"+filename);
            File filepath = new File(uploadFilePath, filename);
            //判断路径是否存在，没有就创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }
            //将上传文件保存到一个目标文档中（文件名是hash名，不是原本的名字）
            try {
                File saveFile = new File(uploadFilePath, filename);
                String saveFilePath = saveFile.getPath();
                logger.info("文件绝对路径：" + saveFilePath);

                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(saveFile));
                out.write(file.getBytes());
                out.flush();
                out.close();
                System.out.println("文件保存完毕");

                attachmentFile.setCreateUserId(getCurrentUser().getId());
                attachmentFile.setFilePath(saveFilePath);
                attachmentFile.setFileContent(file.getBytes());
                attachmentFile.setOriginalName(realname);
                attachmentFile.setCreateDate(new Date());
            } catch (IOException e) {
                logger.error("文件上传发生异常，异常消息：" + e.getMessage());
                result.put("code", 0);
                result.put("msg", "文件上传失败，参数错误");
                return result;
            }
        }
        // 发送消息
        int data = attachmentFileService.insert(attachmentFile);

        result.put("code", 1);
        result.put("msg", "文件上传成功");

        return result;
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
    public Map<String, Object> update(AttachmentFile file, HttpServletRequest request) {

        System.out.println("file = " + file);

        int result = attachmentFileService.updateFileName(file);

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
     * 功能描述: 删除文件
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/22 11:59
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delFile(@RequestParam("id") Integer id) {
        int result = attachmentFileService.delFile(id);

        Map<String, Object> data = Maps.newHashMap();
        if (result > 0) {
            data.put("code", 0);
            data.put("msg", "文件删除成功");
            log.info("文件删除成功");
        } else {
            data.put("code", -1);
            data.put("msg", "文件删除失败");
            log.warn("文件删除失败");
        }
        return data;
    }


}
