package com.reservoir.controller.common;

import com.reservoir.core.entity.Result;
import com.reservoir.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/files")
@Tag(name = "文件接口")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "单文件上传")
    @Async("MinPool")
    public CompletableFuture<Result<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        return CompletableFuture.completedFuture(fileService.uploadFile(file));
    }

    @PostMapping("/uploadMultiple")
    @Operation(summary = "多文件上传")
    @Async("MinPool")
    public CompletableFuture<Result<String>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return CompletableFuture.completedFuture(fileService.uploadMultipleFiles(files));
    }

    @GetMapping("/download/{filename:.+}")
    @Operation(summary = "文件下载")
    @Async("MinPool")
    public CompletableFuture<Result<Resource>> downloadFile(@PathVariable String filename) {
        return CompletableFuture.completedFuture(fileService.loadFileAsResource(filename));
    }
}