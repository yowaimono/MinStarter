package com.reservoir.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private static final ObjectMapper JSON_OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectMapper YAML_OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

    static {
        // 设置JSON ObjectMapper的日期格式
        JSON_OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JSON_OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JSON_OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        JSON_OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // 设置YAML ObjectMapper的日期格式
        YAML_OBJECT_MAPPER.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        YAML_OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        YAML_OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        YAML_OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param obj 要转换的对象
     * @return JSON字符串
     * @throws JsonProcessingException 如果转换失败
     */
    public static String toJson(Object obj) throws JsonProcessingException {
        return JSON_OBJECT_MAPPER.writeValueAsString(obj);
    }

    /**
     * 将JSON字符串转换为对象
     *
     * @param json JSON字符串
     * @param clazz 目标对象类型
     * @param <T> 目标对象类型
     * @return 转换后的对象
     * @throws IOException 如果转换失败
     */
    public static <T> T fromJson(String json, Class<T> clazz) throws IOException {
        return JSON_OBJECT_MAPPER.readValue(json, clazz);
    }

    /**
     * 将JSON字符串转换为对象数组
     *
     * @param json JSON字符串
     * @param clazz 目标对象类型
     * @param <T> 目标对象类型
     * @return 转换后的对象数组
     * @throws IOException 如果转换失败
     */
    public static <T> T[] fromJsonArray(String json, Class<T[]> clazz) throws IOException {
        return JSON_OBJECT_MAPPER.readValue(json, clazz);
    }

    /**
     * 将JSON字符串转换为List
     *
     * @param json JSON字符串
     * @param clazz 目标对象类型
     * @param <T> 目标对象类型
     * @return 转换后的List
     * @throws IOException 如果转换失败
     */
    public static <T> List<T> fromJsonList(String json, Class<T> clazz) throws IOException {
        return JSON_OBJECT_MAPPER.readValue(json, JSON_OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
    }

    /**
     * 将JSON字符串转换为Map
     *
     * @param json JSON字符串
     * @param keyClass key的类型
     * @param valueClass value的类型
     * @param <K> key的类型
     * @param <V> value的类型
     * @return 转换后的Map
     * @throws IOException 如果转换失败
     */
    public static <K, V> Map<K, V> fromJsonMap(String json, Class<K> keyClass, Class<V> valueClass) throws IOException {
        return JSON_OBJECT_MAPPER.readValue(json, JSON_OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass));
    }

    /**
     * 将对象转换为Map
     *
     * @param obj 要转换的对象
     * @param <K> key的类型
     * @param <V> value的类型
     * @return 转换后的Map
     * @throws JsonProcessingException 如果转换失败
     */
    public static <K, V> Map<K, V> toMap(Object obj) throws JsonProcessingException {
        return JSON_OBJECT_MAPPER.convertValue(obj, new TypeReference<Map<K, V>>() {});
    }

    /**
     * 将Map转换为JSON字符串
     *
     * @param map 要转换的Map
     * @return JSON字符串
     * @throws JsonProcessingException 如果转换失败
     */
    public static String mapToJson(Map<?, ?> map) throws JsonProcessingException {
        return JSON_OBJECT_MAPPER.writeValueAsString(map);
    }

    /**
     * 将YAML字符串转换为JSON字符串
     *
     * @param yaml YAML字符串
     * @return JSON字符串
     * @throws IOException 如果转换失败
     */
    public static String yamlToJson(String yaml) throws IOException {
        Object obj = YAML_OBJECT_MAPPER.readValue(yaml, Object.class);
        return JSON_OBJECT_MAPPER.writeValueAsString(obj);
    }

    /**
     * 获取JSON ObjectMapper实例
     *
     * @return JSON ObjectMapper实例
     */
    public static ObjectMapper getJsonObjectMapper() {
        return JSON_OBJECT_MAPPER;
    }

    /**
     * 获取YAML ObjectMapper实例
     *
     * @return YAML ObjectMapper实例
     */
    public static ObjectMapper getYamlObjectMapper() {
        return YAML_OBJECT_MAPPER;
    }


}


