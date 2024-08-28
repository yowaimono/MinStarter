package com.reservoir.service;



import com.reservoir.core.entity.Result;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    Result<String> uploadFile(MultipartFile file);
    Result<String> uploadMultipleFiles(MultipartFile[] files);
    Result<Resource> loadFileAsResource(String filename);
}