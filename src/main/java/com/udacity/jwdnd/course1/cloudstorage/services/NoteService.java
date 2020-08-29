package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNotes(Integer userId){
        return noteMapper.getNotes(userId);
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

    public int deleteNote(Integer noteId){return noteMapper.deleteNote(noteId);}


}
