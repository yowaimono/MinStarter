package com.reservoir.core.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    /**
     * 加载图像文件
     *
     * @param filePath 图像文件的路径
     * @return 加载的 BufferedImage 对象
     * @throws IOException 如果文件不存在或无法读取
     */
    public static BufferedImage loadImage(String filePath) throws IOException {
        return ImageIO.read(new File(filePath));
    }

    /**
     * 保存图像到文件
     *
     * @param image    要保存的 BufferedImage 对象
     * @param filePath 保存的文件路径
     * @param format   图像格式（例如 "png", "jpg"）
     * @throws IOException 如果文件无法写入
     */
    public static void saveImage(BufferedImage image, String filePath, String format) throws IOException {
        ImageIO.write(image, format, new File(filePath));
    }

    /**
     * 调整图像大小
     *
     * @param originalImage 原始图像
     * @param targetWidth   目标宽度
     * @param targetHeight  目标高度
     * @return 调整大小后的 BufferedImage 对象
     */
    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();
        return resizedImage;
    }

    /**
     * 裁剪图像
     *
     * @param originalImage 原始图像
     * @param x             裁剪起始点的 x 坐标
     * @param y             裁剪起始点的 y 坐标
     * @param width         裁剪宽度
     * @param height        裁剪高度
     * @return 裁剪后的 BufferedImage 对象
     */
    public static BufferedImage cropImage(BufferedImage originalImage, int x, int y, int width, int height) {
        return originalImage.getSubimage(x, y, width, height);
    }

    public static void main(String[] args) {
        try {
            // 加载图像
            BufferedImage image = loadImage("path/to/your/image.jpg");

            // 调整图像大小
            BufferedImage resizedImage = resizeImage(image, 300, 200);

            // 保存图像
            saveImage(resizedImage, "path/to/save/resized_image.jpg", "jpg");

            System.out.println("图像处理完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}