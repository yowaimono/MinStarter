package com.reservoir.core.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class CaptchaUtils {

    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;
    private static final int FONT_SIZE = 30;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 生成数字验证码图片
     *
     * @param length 验证码长度
     * @return 验证码图片的字节数组
     * @throws IOException 如果生成图片失败
     */
    public static byte[] generateNumericCaptcha(int length) throws IOException {
        return generateCaptcha(length, "0123456789");
    }

    /**
     * 生成字母验证码图片
     *
     * @param length 验证码长度
     * @return 验证码图片的字节数组
     * @throws IOException 如果生成图片失败
     */
    public static byte[] generateAlphabeticCaptcha(int length) throws IOException {
        return generateCaptcha(length, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }

    /**
     * 生成混合验证码图片
     *
     * @param length 验证码长度
     * @return 验证码图片的字节数组
     * @throws IOException 如果生成图片失败
     */
    public static byte[] generateMixedCaptcha(int length) throws IOException {
        return generateCaptcha(length, CHARACTERS);
    }

    /**
     * 生成验证码图片
     *
     * @param length    验证码长度
     * @param characters 验证码字符集
     * @return 验证码图片的字节数组
     * @throws IOException 如果生成图片失败
     */
    private static byte[] generateCaptcha(int length, String characters) throws IOException {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);
        g2d.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));

        Random random = new Random();
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = characters.charAt(random.nextInt(characters.length()));
            captcha.append(c);
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g2d.drawString(String.valueOf(c), i * (FONT_SIZE - 5) + 10, HEIGHT - 10);
        }

        // 添加干扰线
        for (int i = 0; i < 10; i++) {
            g2d.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            int x1 = random.nextInt(WIDTH);
            int y1 = random.nextInt(HEIGHT);
            int x2 = random.nextInt(WIDTH);
            int y2 = random.nextInt(HEIGHT);
            g2d.drawLine(x1, y1, x2, y2);
        }

        g2d.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }
}