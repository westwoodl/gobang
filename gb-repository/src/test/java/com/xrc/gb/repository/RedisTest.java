package com.xrc.gb.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author xu rongchao
 * @date 2020/3/5 9:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    /**
     * StringRedisTemplate默认选择的StringRedisSerializer序列化器
     */
    @Test
    public void test_redis() {
        redisTemplate.opsForValue().set("xrc", "myValue");
        System.out.println(redisTemplate.opsForValue().get("xrc") + "=====");
    }
}
