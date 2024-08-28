package com.reservoir.core.utils;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalUtils {

    // 定义一个 ThreadLocal 变量
    private static final ThreadLocal<Map<String, Object>> threadLocal = ThreadLocal.withInitial(HashMap::new);

    /**
     * 设置 ThreadLocal 变量的值
     *
     * @param key   键
     * @param value 值
     */
    public static void set(String key, Object value) {
        threadLocal.get().put(key, value);
    }

    /**
     * 获取 ThreadLocal 变量的值
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        return threadLocal.get().get(key);
    }

    /**
     * 移除 ThreadLocal 变量的值
     *
     * @param key 键
     */
    public static void remove(String key) {
        threadLocal.get().remove(key);
    }

    /**
     * 清除 ThreadLocal 变量
     */
    public static void clear() {
        threadLocal.remove();
    }

    /**
     * 获取当前线程的 ThreadLocal 变量中的所有键值对
     *
     * @return 键值对映射
     */
    public static Map<String, Object> getAll() {
        return new HashMap<>(threadLocal.get());
    }
}