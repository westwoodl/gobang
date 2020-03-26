package com.xrc.gb.repository;

import com.xrc.gb.repository.cache.RedisCache;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * @author xu rongchao
 * @date 2020/3/5 9:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {App.class})
public class RedisTest {

    @Resource
    RedisTemplate<String, Object> redisTemplate;


    /**
     * StringRedisTemplate默认选择的StringRedisSerializer序列化器
     */
    @Test
    public void test_redis() {
        redisTemplate.opsForValue().set("xrc", "myValue");
        System.out.println(redisTemplate.opsForValue().get("xrc") + "=====");
    }


    @Test
    public void test_redis_object() {
        A a = new A();
        a.setA(1);
        a.setS("sssss");
        redisTemplate.opsForValue().set("xrc", a);
        System.out.println(redisTemplate.opsForValue().get("xrc") + "=====");
    }


    @Data
    static class A implements Serializable {
        private int a;
        private String s;
    }
}
