package com.reservoir.core.utils;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class VideoRecorderService {

    public void startRecording(String rtspUrl) {
        String outputFile = generateOutputFilePath();

        ProcessBuilder processBuilder = new ProcessBuilder(
                "ffmpeg", "-i", rtspUrl, "-c:v", "libx264", "-preset", "fast", "-crf", "23", "-c:a", "aac", "-f", "hls", "-hls_time", "10", "-hls_list_size", "6", "-hls_flags", "delete_segments", outputFile
        );

        try {
            Process process = processBuilder.start();
            // 你可以在这里处理进程的输出和错误流，或者等待进程完成
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateOutputFilePath() {
        String filename = "output.m3u8";
        Path staticPath = Paths.get("src", "main", "resources", "static", "hls");
        if (!Files.exists(staticPath)) {
            try {
                Files.createDirectories(staticPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return staticPath.resolve(filename).toString();
    }
}