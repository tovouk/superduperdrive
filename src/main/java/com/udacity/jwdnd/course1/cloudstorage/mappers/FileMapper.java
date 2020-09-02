package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.apache.ibatis.annotations.*;
import org.springframework.core.io.Resource;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    public List<File> getFiles(Integer userId);

    @Select("SELECT * FROM FILES WHERE filename = #{filename} AND userid = #{userId}")
    public File getFile(String filename,Integer userId);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId} AND userid = #{userId}")
    public File getFileById(Integer fileId,Integer userId);

    @Insert("INSERT INTO FILES (filename,contenttype,filesize,userid,filedata) VALUES(#{fileName},#{contentType},#{fileSize},#{userId},#{fileData})")
    @Options(useGeneratedKeys = true,keyProperty = "fileId")
    public int insertFile(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    public int deleteFile(Integer fileId);

}
