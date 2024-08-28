package com.reservoir.core.utils;

import com.reservoir.core.exception.MinMapException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.concurrent.*;

@Slf4j
public class MinMap {

    // 静态的ConcurrentHashMap充当全局缓存
    private static final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> permanentCache = new ConcurrentHashMap<>();

    // 用于定期清理过期键值对的ScheduledExecutorService
    private static final ScheduledExecutorService cleaner = Executors.newScheduledThreadPool(1);

    // 用于定期刷盘AOF buffer的ScheduledExecutorService
    private static final ScheduledExecutorService flusher = Executors.newScheduledThreadPool(1);

    // 用于定期重写AOF文件的ScheduledExecutorService
    private static final ScheduledExecutorService aofRewriter = Executors.newScheduledThreadPool(1);

    // AOF文件路径
    private static final String AOF_FILE = "./logs/min/min_aof.log";

    // AOF buffer
    private static final StringBuilder aofBuffer = new StringBuilder();

    // 私有构造函数，防止实例化
    private MinMap() {}

    static {
        // 每秒钟检查一次过期键值对
        cleaner.scheduleAtFixedRate(MinMap::cleanup, 1, 1, TimeUnit.SECONDS);
        // 每秒钟刷盘一次AOF buffer
        flusher.scheduleAtFixedRate(MinMap::flushAofBuffer, 1, 1, TimeUnit.SECONDS);
        // 每分钟重写一次AOF文件
        aofRewriter.scheduleAtFixedRate(MinMap::rewriteAofFile, 1, 60, TimeUnit.SECONDS);
        // 启动时恢复持久化数据
        restore();
    }

    /**
     * 设置缓存中的键值对，并指定过期时间
     * @param key       键
     * @param value     值
     * @param duration  过期时间，单位为秒
     */
    public static void set(String key, String value, Long duration) {
        try {
            if (duration != null && duration > 0) {
                long expirationTime = System.currentTimeMillis() + duration * 1000L;
                CacheEntry entry = new CacheEntry(value, expirationTime);
                cache.put(key, entry);
                permanentCache.remove(key); // 如果存在永久缓存，移除
                appendToAofBuffer("SET", key, value, String.valueOf(expirationTime));
            } else {
                permanentCache.put(key, value);
                cache.remove(key); // 如果存在过期缓存，移除
                appendToAofBuffer("SET_PERMANENT", key, value);
            }
        } catch (Exception e) {
            throw new MinMapException("Error setting key-value pair", e);
        }
    }

    /**
     * 设置缓存中的键值对，无过期时间
     * @param key   键
     * @param value 值
     */
    public static void set(String key, String value) {
        set(key, value, null);
    }

    /**
     * 获取缓存中的值
     * @param key 键
     * @return 值，如果不存在或已过期则返回null
     */
    public static String get(String key) {
        try {
            CacheEntry entry = cache.get(key);
            if (entry != null && !entry.isExpired()) {
                return entry.getValue();
            }
            return permanentCache.get(key);
        } catch (Exception e) {
            throw new MinMapException("Error getting value for key: " + key, e);
        }
    }

    /**
     * 删除缓存中的键值对
     * @param key 键
     */
    public static void delete(String key) {
        try {
            cache.remove(key);
            permanentCache.remove(key);
            appendToAofBuffer("DELETE", key);
        } catch (Exception e) {
            throw new MinMapException("Error deleting key: " + key, e);
        }
    }

    /**
     * 检查缓存中是否存在指定的键
     * @param key 键
     * @return 如果存在且未过期则返回true，否则返回false
     */
    public static boolean containsKey(String key) {
        try {
            CacheEntry entry = cache.get(key);
            return (entry != null && !entry.isExpired()) || permanentCache.containsKey(key);
        } catch (Exception e) {
            throw new MinMapException("Error checking if key exists: " + key, e);
        }
    }

    /**
     * 获取缓存的大小
     * @return 缓存的大小
     */
    public static int size() {
        try {
            return cache.size() + permanentCache.size();
        } catch (Exception e) {
            throw new MinMapException("Error getting cache size", e);
        }
    }

    /**
     * 清空缓存
     */
    public static void clear() {
        try {
            cache.clear();
            permanentCache.clear();
            appendToAofBuffer("CLEAR");
        } catch (Exception e) {
            throw new MinMapException("Error clearing cache", e);
        }
    }

    /**
     * 定期清理过期的键值对
     */
    private static void cleanup() {
        try {
            long now = System.currentTimeMillis();
            cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
        } catch (Exception e) {
            log.error("Error cleaning up expired entries", e);
        }
    }

    /**
     * 将操作追加到AOF buffer
     * @param command 操作命令
     * @param args 操作参数
     */
    private static void appendToAofBuffer(String command, String... args) {
        synchronized (aofBuffer) {
            try {
                aofBuffer.append(command).append("\n");
                for (String arg : args) {
                    aofBuffer.append(arg).append("\n");
                }
            } catch (Exception e) {
                log.error("Error appending to AOF buffer", e);
            }
        }
    }

    /**
     * 定期刷盘AOF buffer
     */
    private static void flushAofBuffer() {
        synchronized (aofBuffer) {
            if (aofBuffer.length() > 0) {
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(AOF_FILE, true))) {
                    bos.write(aofBuffer.toString().getBytes());
                    bos.flush();
                    aofBuffer.setLength(0); // 清空buffer
                } catch (IOException e) {
                    log.error("Error flushing AOF buffer", e);
                }
            }
        }
    }

    /**
     * 从AOF文件恢复缓存
     */
    public static void restore() {
        File file = new File(AOF_FILE);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String command = line;
                    switch (command) {
                        case "SET":
                            String key = br.readLine();
                            String value = br.readLine();
                            long expirationTime = Long.parseLong(br.readLine());
                            if (System.currentTimeMillis() < expirationTime) {
                                cache.put(key, new CacheEntry(value, expirationTime));
                            }
                            break;
                        case "SET_PERMANENT":
                            key = br.readLine();
                            value = br.readLine();
                            permanentCache.put(key, value);
                            break;
                        case "DELETE":
                            key = br.readLine();
                            cache.remove(key);
                            permanentCache.remove(key);
                            break;
                        case "CLEAR":
                            cache.clear();
                            permanentCache.clear();
                            break;
                        default:
                            log.warn("Unknown command in AOF file: {}", command);
                            break;
                    }
                }
            } catch (IOException e) {
                log.error("Error restoring cache from AOF file", e);
            }
        }
    }

    /**
     * 定期重写AOF文件
     */
    private static void rewriteAofFile() {
        log.info("Rewriting AOF file...");
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(AOF_FILE, false))) {
            for (String key : cache.keySet()) {
                CacheEntry entry = cache.get(key);
                bos.write(("SET\n" + key + "\n" + entry.getValue() + "\n" + entry.getExpirationTime() + "\n").getBytes());
            }
            for (String key : permanentCache.keySet()) {
                String value = permanentCache.get(key);
                bos.write(("SET_PERMANENT\n" + key + "\n" + value + "\n").getBytes());
            }
            bos.flush();
        } catch (IOException e) {
            log.error("Error rewriting AOF file", e);
        }
    }

    // 内部类，用于存储缓存条目及其过期时间
    private static class CacheEntry implements Serializable {
        private final String value;
        private final long expirationTime;

        public CacheEntry(String value, long expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
        }

        public String getValue() {
            return value;
        }

        public long getExpirationTime() {
            return expirationTime;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }
}