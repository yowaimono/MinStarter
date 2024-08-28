package com.reservoir.core.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncryptionUtil {

    /**
     * 使用SHA-256算法加密字符串
     *
     * @param input 输入字符串
     * @return 加密后的字符串
     */
    public static String sha256(String input) {
        return hash("SHA-256", input);
    }

    /**
     * 使用MD5算法加密字符串
     *
     * @param input 输入字符串
     * @return 加密后的字符串
     */
    public static String md5(String input) {
        return hash("MD5", input);
    }

    /**
     * 使用带盐的SHA-256算法加密字符串
     *
     * @param input 输入字符串
     * @param salt  盐值
     * @return 加密后的字符串
     */
    public static String sha256WithSalt(String input, byte[] salt) {
        return hashWithSalt("SHA-256", input, salt);
    }

    /**
     * 生成随机盐值
     *
     * @return 盐值字节数组
     */
    public static byte[] generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    /**
     * 使用指定算法加密字符串
     *
     * @param algorithm 算法名称
     * @param input     输入字符串
     * @return 加密后的字符串
     */
    private static String hash(String algorithm, String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(algorithm + " algorithm not found", e);
        }
    }

    /**
     * 使用指定算法和盐值加密字符串
     *
     * @param algorithm 算法名称
     * @param input     输入字符串
     * @param salt      盐值
     * @return 加密后的字符串
     */
    private static String hashWithSalt(String algorithm, String input, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(salt);
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(algorithm + " algorithm not found", e);
        }
    }

    /**
     * 验证SHA-256加密的密码明文和密文是否匹配
     *
     * @param plainTextPassword 密码明文
     * @param hashedPassword    加密后的密码（密文）
     * @return 是否匹配
     */
    public static boolean verifySha256Password(String plainTextPassword, String hashedPassword) {
        String rehashedPassword = sha256(plainTextPassword);
        return rehashedPassword.equals(hashedPassword);
    }

    /**
     * 验证MD5加密的密码明文和密文是否匹配
     *
     * @param plainTextPassword 密码明文
     * @param hashedPassword    加密后的密码（密文）
     * @return 是否匹配
     */
    public static boolean verifyMd5Password(String plainTextPassword, String hashedPassword) {
        String rehashedPassword = md5(plainTextPassword);
        return rehashedPassword.equals(hashedPassword);
    }

    /**
     * 验证带盐的SHA-256加密的密码明文和密文是否匹配
     *
     * @param plainTextPassword 密码明文
     * @param hashedPassword    加密后的密码（密文）
     * @param salt              盐值
     * @return 是否匹配
     */
    public static boolean verifySha256WithSaltPassword(String plainTextPassword, String hashedPassword, byte[] salt) {
        String rehashedPassword = sha256WithSalt(plainTextPassword, salt);
        return rehashedPassword.equals(hashedPassword);
    }
}