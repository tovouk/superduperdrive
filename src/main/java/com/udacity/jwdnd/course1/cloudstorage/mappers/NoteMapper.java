package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    public List<Note> getNotes(Integer userId);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteId}")
    public Note getNote(Integer noteId);

    @Insert("INSERT INTO NOTES (notetitle,notedescription,userid) VALUES(#{noteTitle},#{noteDescription},#{userId})")
    @Options(useGeneratedKeys = true,keyProperty = "noteId")
    public int insertNote(Note note);

    @Update("UPDATE NOTES SET notetitle=#{noteTitle}, notedescription=#{noteDescription} WHERE noteid = #{noteId}")
    public int updateNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    public int deleteNote(Integer noteId);

}
