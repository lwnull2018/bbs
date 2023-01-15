package com.bbs.remark.dao;

import com.bbs.remark.pojo.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface NoteMapper {

    //获取所有未被删除的留言
    List<Note> getAll();

    //添加一条留言
    int addNote(@Param("note") Note note);
}

