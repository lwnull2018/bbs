package com.bbs.remark.service;

import com.bbs.remark.dao.NoteMapper;
import com.bbs.remark.pojo.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;

    private NoteMapper getMapper() {
        return noteMapper;
    }

    public int addNote(Note note) {
        return getMapper().addNote(note);
    }

    public List<Note> getAll() {
        return getMapper().getAll();
    }
}


