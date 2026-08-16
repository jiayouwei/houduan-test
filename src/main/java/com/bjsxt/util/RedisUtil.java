package com.bjsxt.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
