package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mappers.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;
    private final UserMapper userMapper;

    public NoteService(NoteMapper noteMapper,UserMapper userMapper) {
        this.noteMapper = noteMapper;
        this.userMapper = userMapper;
    }

    public List<Note> getNotes(){
        return noteMapper.getNotes();
    }

    public Note getNote(Integer noteId){
        return noteMapper.getNote(noteId);
    }

    public int createNote(Note note){
        return noteMapper.insertNote(new Note(null,note.getNoteTitle(),note.getNoteDescription(),note.getUserId()));
    }

    public int updateNote(Note note){
        return noteMapper.updateNote(note);
    }




}
