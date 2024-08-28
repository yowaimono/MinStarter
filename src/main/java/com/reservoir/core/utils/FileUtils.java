package com.reservoir.core.utils;



import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


@Slf4j
public class FileUtils {

    /**
     * 读取文件内容到字符串
     *
     * @param filePath 文件路径
     * @return 文件内容字符串
     * @throws IOException 如果文件读取失败
     */
    public static String readFileToString(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    /**
     * 将字符串写入文件
     *
     * @param filePath 文件路径
     * @param content  要写入的内容
     * @throws IOException 如果文件写入失败
     */
    public static void writeStringToFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes());
    }

    /**
     * 复制文件
     *
     * @param sourceFilePath 源文件路径
     * @param destFilePath   目标文件路径
     * @throws IOException 如果文件复制失败
     */
    public static void copyFile(String sourceFilePath, String destFilePath) throws IOException {
        Files.copy(Paths.get(sourceFilePath), Paths.get(destFilePath), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 移动文件
     *
     * @param sourceFilePath 源文件路径
     * @param destFilePath   目标文件路径
     * @throws IOException 如果文件移动失败
     */
    public static void moveFile(String sourceFilePath, String destFilePath) throws IOException {
        Files.move(Paths.get(sourceFilePath), Paths.get(destFilePath), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @throws IOException 如果文件删除失败
     */
    public static void deleteFile(String filePath) throws IOException {
        Files.deleteIfExists(Paths.get(filePath));
    }

    /**
     * 检查文件是否存在
     *
     * @param filePath 文件路径
     * @return 文件是否存在
     */
    public static boolean exists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * 读取文件内容到列表
     *
     * @param filePath 文件路径
     * @return 文件内容列表
     * @throws IOException 如果文件读取失败
     */
    public static List<String> readLines(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }

    /**
     * 将列表内容写入文件
     *
     * @param filePath 文件路径
     * @param lines    要写入的列表内容
     * @throws IOException 如果文件写入失败
     */
    public static void writeLines(String filePath, List<String> lines) throws IOException {
        Files.write(Paths.get(filePath), lines);
    }

    /**
     * 获取文件大小
     *
     * @param filePath 文件路径
     * @return 文件大小（字节）
     * @throws IOException 如果获取文件大小失败
     */
    public static long getFileSize(String filePath) throws IOException {
        return Files.size(Paths.get(filePath));
    }

    /**
     * 创建目录
     *
     * @param dirPath 目录路径
     * @throws IOException 如果目录创建失败
     */
    public static void createDirectory(String dirPath) throws IOException {
        Files.createDirectories(Paths.get(dirPath));
    }

    /**
     * 列出目录中的所有文件
     *
     * @param dirPath 目录路径
     * @return 文件列表
     * @throws IOException 如果列出文件失败
     */
    public static List<File> listFiles(String dirPath) throws IOException {
        List<File> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dirPath))) {
            for (Path path : stream) {
                files.add(path.toFile());
            }
        }
        return files;
    }

    /**
     * 判断文件是否为图片（PNG、BMP、JPG）
     *
     * @param filePath 文件路径
     * @return 是否为图片
     * @throws IOException 如果文件读取失败
     */
    public static boolean isImageFile(String filePath) throws IOException {
        String mimeType = Files.probeContentType(Paths.get(filePath));
        return mimeType != null && (mimeType.equals("image/png") || mimeType.equals("image/bmp") || mimeType.equals("image/jpeg"));
    }


    /**
     * 判断文件是否为PPTX
     *
     * @param filePath 文件路径
     * @return 是否为PPTX
     */
    public static boolean isPPTX(String filePath) {
        String mimeType = getMimeType(filePath);
        return "application/vnd.openxmlformats-officedocument.presentationml.presentation".equals(mimeType);
    }

    /**
     * 判断文件是否为DOCX
     *
     * @param filePath 文件路径
     * @return 是否为DOCX
     */
    public static boolean isDocx(String filePath) {
        String mimeType = getMimeType(filePath);
        return "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(mimeType);
    }

    /**
     * 判断文件是否为PNG
     *
     * @param filePath 文件路径
     * @return 是否为PNG
     */
    public static boolean isPng(String filePath) {
        String mimeType = getMimeType(filePath);
        return "image/png".equals(mimeType);
    }

    /**
     * 判断文件是否为文本文件
     *
     * @param filePath 文件路径
     * @return 是否为文本文件
     */
    public static boolean isText(String filePath) {
        String mimeType = getMimeType(filePath);
        return mimeType != null && mimeType.startsWith("text");
    }

    /**
     * 获取文件的MIME类型
     *
     * @param filePath 文件路径
     * @return MIME类型
     */
    private static String getMimeType(String filePath) {
        try {
            return Files.probeContentType(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回文件的Base64编码
     *
     * @param filePath 文件路径
     * @return 文件的Base64编码字符串
     * @throws IOException 如果文件读取失败
     */
    public static String getFileBase64(String filePath) throws IOException {
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        return Base64.getEncoder().encodeToString(fileBytes);
    }

    /**
     * 将Base64编码字符串解码并写入文件
     *
     * @param base64String Base64编码字符串
     * @param filePath     目标文件路径
     * @throws IOException 如果文件写入失败
     */
    public static void writeBase64ToFile(String base64String, String filePath) throws IOException {
        byte[] fileBytes = Base64.getDecoder().decode(base64String);
        Files.write(Paths.get(filePath), fileBytes);
    }




    /**
     * 返回字节数组的Base64编码
     *
     * @param bytes 字节数组
     * @return 字节数组的Base64编码字符串
     */
    public static String getBytesBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }


    /**
     * 将Base64编码字符串解码并写入输出流
     *
     * @param base64String Base64编码字符串
     * @param outputStream 输出流
     * @throws IOException 如果写入输出流失败
     */
    public static void writeBase64ToOutputStream(String base64String, OutputStream outputStream) throws IOException {
        byte[] fileBytes = Base64.getDecoder().decode(base64String);
        outputStream.write(fileBytes);
        outputStream.flush();
    }


    /**
     * 将输入流的内容复制到输出流
     *
     * @param inputStream  输入流
     * @param outputStream 输出流
     * @throws IOException 如果读写流失败
     */
    public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
    }

    /**
     * 将输入流的内容读取为字节数组
     *
     * @param inputStream 输入流
     * @return 字节数组
     * @throws IOException 如果读取流失败
     */
    public static byte[] readStreamToBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 将输入流的内容读取为字符串
     *
     * @param inputStream 输入流
     * @return 字符串
     * @throws IOException 如果读取流失败
     */
    public static String readStreamToString(InputStream inputStream) throws IOException {
        return new String(readStreamToBytes(inputStream));
    }

    /**
     * 将字节数组写入输出流
     *
     * @param bytes        字节数组
     * @param outputStream 输出流
     * @throws IOException 如果写入流失败
     */
    public static void writeBytesToStream(byte[] bytes, OutputStream outputStream) throws IOException {
        outputStream.write(bytes);
        outputStream.flush();
    }

    /**
     * 将字符串写入输出流
     *
     * @param str          字符串
     * @param outputStream 输出流
     * @throws IOException 如果写入流失败
     */
    public static void writeStringToStream(String str, OutputStream outputStream) throws IOException {
        outputStream.write(str.getBytes());
        outputStream.flush();
    }

    /**
     * 将文件保存到classpath:static目录下
     *
     * @param filePath    文件路径
     * @param fileName    文件名
     * @throws IOException 如果文件保存失败
     */
    public static void saveFileToClasspathStatic(String filePath, String fileName) throws IOException {
        URL resource = FileUtils.class.getClassLoader().getResource("static");
        if (resource == null) {
            throw new IOException("Static directory not found in classpath");
        }
        String staticDirPath = resource.getPath();
        Files.copy(Paths.get(filePath), Paths.get(staticDirPath, fileName));
    }


    public static void downLoad(String url, String path, HttpServletResponse response) {
        InputStream in = null;
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(100000);
            conn.setReadTimeout(200000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.connect();
            in = conn.getInputStream();
            byte[] bs = new byte[1024];
            int len = 0;
            response.reset();
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/octet-stream");
            String fileName = url.replaceAll(path + "/", "");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            ServletOutputStream out = response.getOutputStream();
            while ((len = in.read(bs)) != -1) {
                out.write(bs, 0, len);
            }
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(url + "下载失败");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String getContentType(String filenameExtension) {
        if (filenameExtension.equalsIgnoreCase(".pdf")) {
            return "application/pdf";
        }
        if (filenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (filenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (filenameExtension.equalsIgnoreCase(".jpeg") ||
                filenameExtension.equalsIgnoreCase(".jpg") ||
                filenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        if (filenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (filenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (filenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (filenameExtension.equalsIgnoreCase(".pptx") ||
                filenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (filenameExtension.equalsIgnoreCase(".docx")) {
            return "application/msword";
        }
        if (filenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpg";
    }






    /**
     * 写文件到response
     *
     * @param response response
     * @param filePath 文件路径
     */
    public static void readFile(HttpServletResponse response, String filePath) {
        if (!StringUtils.hasLength(filePath)) {
            return;
        }
        if (filePath.contains("../") || filePath.contains("..\\")) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        FileInputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(file);
            byte[] byteData = new byte[1024];
            out = response.getOutputStream();
            int len = 0;
            while ((len = in.read(byteData)) != -1) {
                out.write(byteData, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("IO异常", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("IO异常", e);
                }
            }
        }
    }

    /**
     * 重命名文件
     *
     * @param oldFilePath 旧文件路径
     * @param newFilePath 新文件路径
     * @return 是否重命名成功
     */
    public static boolean renameFile(String oldFilePath, String newFilePath) {
        if (!StringUtils.hasLength(oldFilePath) || !StringUtils.hasLength(newFilePath)) {
            return false;
        }
        File oldFile = new File(oldFilePath);
        File newFile = new File(newFilePath);
        if (oldFile.exists()) {
            return oldFile.renameTo(newFile);
        }
        return false;
    }


}