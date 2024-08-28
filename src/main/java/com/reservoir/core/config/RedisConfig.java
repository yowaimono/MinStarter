package com.reservoir.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {



    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 设置key的序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        // 设置value的序列化方式
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 设置hash的key的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        // 设置hash的value的序列化方式
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

//    @Bean
//    public RedisTemplate<String, String> redisStringTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, String> template = new RedisTemplate<>();
//        template.setConnectionFactory(redisConnectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new StringRedisSerializer());
//        return template;
//    }
}