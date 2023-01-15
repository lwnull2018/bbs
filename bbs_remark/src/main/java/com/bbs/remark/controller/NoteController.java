package com.bbs.remark.controller;

import cn.hutool.core.date.DateUtil;
import com.bbs.remark.pojo.Note;
import com.bbs.remark.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@CrossOrigin	//为了便于调试，特别在此开启跨域
@RequestMapping("note")
public class NoteController {

    @Autowired
    NoteService ns;

    @RequestMapping("/")
    public String getModel(Model model){
        model.addAttribute("key","这是一个键");
        return "note.html";
    }

    /*添加一条元留言，需要传递参数：
        content:内容
        nickname:昵称
    */
    @RequestMapping("addMetaNote")
    public int addMetaNote(Note note) {

        System.out.println("加入一条元留言：" + note);
        /*为元留言设置基础额外信息*/
        note.setCreateTime(DateUtil.now());
        note.setReplyPost("0");
        note.setResId("0");
        note.setRespondent("");

        return ns.addNote(note);

    }

    /*添加一条回复，需要传递参数
        content:内容
        nickname:昵称
        resId:回复对象id
        replyPost:根留言id
    */
    @RequestMapping("addFollowNote")
    public int addFollowNote(Note note) {

        System.out.println("加入一条回复留言：" + note);

        note.setCreateTime(DateUtil.now());


        return ns.addNote(note);
    }

    @RequestMapping("getAll")
    public List<Note> getAll(){
        System.out.println("获取所有评论列表");

        return ns.getAll();
    }
}

