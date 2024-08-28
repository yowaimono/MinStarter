package com.reservoir.service.Impl;




import com.reservoir.core.entity.Result;
import com.reservoir.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileService {

    private final Path fileStorageLocation;

    public FileServiceImpl() {
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public Result<String> uploadFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return Result.success("File uploaded successfully: " + fileName);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Override
    public Result<String> uploadMultipleFiles(MultipartFile[] files) {
        StringBuilder message = new StringBuilder();
        for (MultipartFile file : files) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                message.append("File uploaded successfully: ").append(fileName).append("\n");
            } catch (IOException ex) {
                throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
            }
        }
        return Result.success(message.toString());
    }

    @Override
    public Result<Resource> loadFileAsResource(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return Result.success(resource);
            } else {
                throw new RuntimeException("File not found " + filename);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + filename, ex);
        }
    }
}