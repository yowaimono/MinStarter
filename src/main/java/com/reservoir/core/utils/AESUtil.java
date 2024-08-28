package com.reservoir.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
@Slf4j
public class AESUtil {
    private static final String ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 128;

    private SecretKey currentSecretKey;
    private SecretKey previousSecretKey;
    private byte[] currentIv;
    private byte[] previousIv;
    private String currentVersion;
    private String previousVersion;

    public AESUtil() throws NoSuchAlgorithmException {
        log.info("Initializing AESUtil...");
        initializeKeyAndIV();
    }

    private void initializeKeyAndIV() throws NoSuchAlgorithmException {
        log.info("Attempting to load keys and IVs from MinMap...");

        String currentSecretKeyBase64 = MinMap.get("aes.currentSecretKey");
        String currentIvBase64 = MinMap.get("aes.currentIv");
        String currentVersionHex = MinMap.get("aes.currentVersion");

        String previousSecretKeyBase64 = MinMap.get("aes.previousSecretKey");
        String previousIvBase64 = MinMap.get("aes.previousIv");
        String previousVersionHex = MinMap.get("aes.previousVersion");

        if (currentSecretKeyBase64 != null && currentIvBase64 != null && currentVersionHex != null) {
            log.info("Restoring current key, IV, and version from MinMap.");
            this.currentSecretKey = new SecretKeySpec(Base64.getDecoder().decode(currentSecretKeyBase64), ALGORITHM);
            this.currentIv = Base64.getDecoder().decode(currentIvBase64);
            this.currentVersion = currentVersionHex;

            if (previousSecretKeyBase64 != null && previousIvBase64 != null && previousVersionHex != null) {
                log.info("Restoring previous key, IV, and version from MinMap.");
                this.previousSecretKey = new SecretKeySpec(Base64.getDecoder().decode(previousSecretKeyBase64), ALGORITHM);
                this.previousIv = Base64.getDecoder().decode(previousIvBase64);
                this.previousVersion = previousVersionHex;
            }
        } else {
            log.warn("No keys found in MinMap. Generating new keys...");
            generateKeyAndIV();
            this.currentVersion = "0001";
            saveKeyAndIVToMinMap();
        }
    }

    private void generateKeyAndIV() throws NoSuchAlgorithmException {
        log.info("Generating new key and IV...");
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE);

        this.previousSecretKey = this.currentSecretKey;
        this.previousIv = this.currentIv;
        this.previousVersion = this.currentVersion;

        this.currentSecretKey = keyGenerator.generateKey();
        this.currentIv = generateIV();

        log.info("New key and IV generated.");
    }

    private byte[] generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        log.info("Generated new IV: {}", Base64.getEncoder().encodeToString(iv));
        return iv;
    }

    private void saveKeyAndIVToMinMap() {
        log.info("Saving current and previous keys, IVs, and versions to MinMap...");

        String currentSecretKeyBase64 = Base64.getEncoder().encodeToString(this.currentSecretKey.getEncoded());
        String currentIvBase64 = Base64.getEncoder().encodeToString(this.currentIv);
        MinMap.set("aes.currentSecretKey", currentSecretKeyBase64, null);
        MinMap.set("aes.currentIv", currentIvBase64, null);
        MinMap.set("aes.currentVersion", this.currentVersion, null);

        if (this.previousSecretKey != null && this.previousIv != null) {
            String previousSecretKeyBase64 = Base64.getEncoder().encodeToString(this.previousSecretKey.getEncoded());
            String previousIvBase64 = Base64.getEncoder().encodeToString(this.previousIv);
            MinMap.set("aes.previousSecretKey", previousSecretKeyBase64, null);
            MinMap.set("aes.previousIv", previousIvBase64, null);
            MinMap.set("aes.previousVersion", this.previousVersion, null);
        }

        log.info("Keys and IVs successfully saved to MinMap.");
    }

    private void incrementVersion() {
        int currentVersionInt = Integer.parseInt(this.currentVersion, 16);
        currentVersionInt++;
        this.currentVersion = String.format("%04X", currentVersionInt);
        log.info("Version incremented to: {}", this.currentVersion);
    }

    @Scheduled(cron = "0 0 0 1 * ?") // 每月1号的午夜执行
    public void scheduledKeyRotation() throws NoSuchAlgorithmException {
        log.info("Scheduled key rotation triggered.");
        generateKeyAndIV();
        incrementVersion();
        saveKeyAndIVToMinMap();
        log.info("Key rotation completed.");
    }

    public String getCurrentVersion() {
        return this.currentVersion;
    }

    public String encrypt(String plainText) throws Exception {
        log.info("Encrypting data with version: {}", this.currentVersion);
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        SecretKeySpec keySpec = new SecretKeySpec(this.currentSecretKey.getEncoded(), ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(this.currentIv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        String encryptedText = this.currentVersion + Base64.getEncoder().encodeToString(encryptedBytes);
        log.info("Data encrypted successfully. Encrypted text: {}", encryptedText);
        return encryptedText;
    }

    public String decrypt(String encryptedText) throws Exception {
        log.info("Decrypting data...");
        String version = encryptedText.substring(0, 4);
        String encryptedData = encryptedText.substring(4);

        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        SecretKeySpec keySpec;
        IvParameterSpec ivSpec;

        if (version.equals(this.currentVersion)) {
            log.info("Using current key for decryption. Version: {}", this.currentVersion);
            keySpec = new SecretKeySpec(this.currentSecretKey.getEncoded(), ALGORITHM);
            ivSpec = new IvParameterSpec(this.currentIv);
        } else if (version.equals(this.previousVersion)) {
            log.info("Using previous key for decryption. Version: {}", this.previousVersion);
            keySpec = new SecretKeySpec(this.previousSecretKey.getEncoded(), ALGORITHM);
            ivSpec = new IvParameterSpec(this.previousIv);
        } else {
            log.error("Unrecognized key version: {}", version);
            throw new IllegalArgumentException("无法识别的密钥版本");
        }

        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        String decryptedText = new String(decryptedBytes, StandardCharsets.UTF_8);
        log.info("Data decrypted successfully. Decrypted text: {}", decryptedText);
        return decryptedText;
    }
}
