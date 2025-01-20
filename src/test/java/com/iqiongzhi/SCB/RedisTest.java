package com.iqiongzhi.SCB;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testRedisConnection() {
        // 写入数据到 Redis
        redisTemplate.opsForValue().set("testKey", "testValue", 5, TimeUnit.MINUTES);

        // 从 Redis 读取数据
        String value = redisTemplate.opsForValue().get("testKey");

        // 打印读取的值
        System.out.println("Value from Redis: " + value);

        // 验证数据一致性
        assertEquals("testValue", value);
    }
}
