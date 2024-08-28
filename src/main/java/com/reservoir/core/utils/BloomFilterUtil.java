package com.reservoir.core.utils;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
public class BloomFilterUtil {

    private static StringRedisTemplate redisTemplate;
    private String key;
    private int size;
    private int hashCount;

    // 设置默认的 Bloom filter 大小和哈希函数数量
    private static final int DEFAULT_SIZE = 10000;
    private static final int DEFAULT_HASH_COUNT = 3;
    private static final String DEFAULT_KEY = "defaultBloomFilter";

    // 私有构造函数，防止外部创建实例
    private BloomFilterUtil() {}

    // 内部静态类，负责持有 BloomFilterUtil 的唯一实例
    private static class Holder {
        private static final BloomFilterUtil INSTANCE = new BloomFilterUtil();
    }

    // 获取唯一实例
    public static BloomFilterUtil getBF(String key) {
        BloomFilterUtil instance = Holder.INSTANCE;
        instance.key = key;
        instance.size = DEFAULT_SIZE;
        instance.hashCount = DEFAULT_HASH_COUNT;
        log.info("Created BloomFilter with default size and hashCount for key: {}", key);
        return instance;
    }

    // 可以自定义大小和哈希函数数量的重载方法
    public static BloomFilterUtil getBF(String key, int size, int hashCount) {
        BloomFilterUtil instance = Holder.INSTANCE;
        instance.key = key;
        instance.size = size;
        instance.hashCount = hashCount;
        log.info("Created BloomFilter with custom size {} and hashCount {} for key: {}", size, hashCount, key);
        return instance;
    }

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        BloomFilterUtil.redisTemplate = redisTemplate;
        log.info("RedisTemplate injected into BloomFilterUtil");
    }

    @PostConstruct
    public void init() {
        // 可以在这里执行任何初始化逻辑
        log.info("BloomFilterUtil initialized");
    }

    // 添加元素到默认布隆过滤器
    public static boolean setDefault(String item) {
        BloomFilterUtil instance = getBF(DEFAULT_KEY);
        return instance.set(item);
    }

    // 批量添加元素到默认布隆过滤器
    public static boolean setDefault(String... items) {
        BloomFilterUtil instance = getBF(DEFAULT_KEY);
        return instance.set(items);
    }

    // 检查元素是否存在于默认布隆过滤器
    public static boolean containsDefault(String item) {
        BloomFilterUtil instance = getBF(DEFAULT_KEY);
        return instance.contains(item);
    }

    // 批量检查元素是否存在于默认布隆过滤器
    public static boolean containsDefault(String... items) {
        BloomFilterUtil instance = getBF(DEFAULT_KEY);
        return instance.contains(items);
    }

    // 添加元素到布隆过滤器
    public boolean set(String item) {
        try {
            log.debug("Setting item {} in BloomFilter with key: {}", item, key);
            for (int seed = 0; seed < hashCount; seed++) {
                int index = hash(item, seed);
                redisTemplate.opsForValue().setBit(key, index, true);
                log.trace("Set bit at index {} for item {}", index, item);
            }
            log.debug("Item {} set successfully in BloomFilter with key: {}", item, key);
            return true;
        } catch (Exception e) {
            log.error("Failed to set item {} in BloomFilter with key: {}", item, key, e);
            return false;
        }
    }

    // 批量添加元素到布隆过滤器
    public boolean set(String... items) {
        try {
            log.debug("Setting multiple items in BloomFilter with key: {}", key);
            for (String item : items) {
                for (int seed = 0; seed < hashCount; seed++) {
                    int index = hash(item, seed);
                    redisTemplate.opsForValue().setBit(key, index, true);
                    log.trace("Set bit at index {} for item {}", index, item);
                }
            }
            log.debug("Multiple items set successfully in BloomFilter with key: {}", key);
            return true;
        } catch (Exception e) {
            log.error("Failed to set multiple items in BloomFilter with key: {}", key, e);
            return false;
        }
    }

    // 检查元素是否存在于布隆过滤器
    public boolean contains(String item) {
        log.debug("Checking if item {} exists in BloomFilter with key: {}", item, key);
        for (int seed = 0; seed < hashCount; seed++) {
            int index = hash(item, seed);
            if (Boolean.FALSE.equals(redisTemplate.opsForValue().getBit(key, index))) {
                log.debug("Item {} does not exist in BloomFilter with key: {}", item, key);
                return false;
            }
            log.trace("Checked bit at index {} for item {}", index, item);
        }
        log.debug("Item {} exists in BloomFilter with key: {}", item, key);
        return true;
    }

    // 批量检查元素是否存在于布隆过滤器
    public boolean contains(String... items) {
        log.debug("Checking if multiple items exist in BloomFilter with key: {}", key);
        for (String item : items) {
            for (int seed = 0; seed < hashCount; seed++) {
                int index = hash(item, seed);
                if (Boolean.FALSE.equals(redisTemplate.opsForValue().getBit(key, index))) {
                    log.debug("Item {} does not exist in BloomFilter with key: {}", item, key);
                    return false;
                }
                log.trace("Checked bit at index {} for item {}", index, item);
            }
        }
        log.debug("All items exist in BloomFilter with key: {}", key);
        return true;
    }

    // 删除布隆过滤器
    public boolean delete() {
        try {
            log.info("Deleting BloomFilter with key: {}", key);
            return redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Failed to delete BloomFilter with key: {}", key, e);
            return false;
        }
    }

    // 重置布隆过滤器
    public boolean reset() {
        try {
            log.info("Resetting BloomFilter with key: {}", key);
            redisTemplate.delete(key);
            return true;
        } catch (Exception e) {
            log.error("Failed to reset BloomFilter with key: {}", key, e);
            return false;
        }
    }

    // 哈希函数
    private int hash(String item, int seed) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest((item + seed).getBytes(StandardCharsets.UTF_8));
            int hashValue = Math.abs(bytesToInt(digest)) % size;
            log.trace("Hashed item {} with seed {} to index {}", item, seed, hashValue);
            return hashValue;
        } catch (NoSuchAlgorithmException e) {
            log.error("Hash algorithm not found", e);
            throw new RuntimeException("Hash algorithm not found", e);
        }
    }

    // 辅助方法，将字节数组转换为 int
    private int bytesToInt(byte[] bytes) {
        int value = 0;
        for (byte b : bytes) {
            value = (value << 8) | (b & 0xFF);
        }
        return value;
    }
}