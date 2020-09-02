package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFiles(Integer userId){return fileMapper.getFiles(userId);}

    public File getFile(String filename, Integer userId){return fileMapper.getFile(filename,userId);}

    public File getFileById(Integer fileId, Integer userId){return fileMapper.getFileById(fileId,userId);}

    public int insertFile(File file){return fileMapper.insertFile(file);}

    public int deleteFile(Integer fileId){return fileMapper.deleteFile(fileId);}

}
