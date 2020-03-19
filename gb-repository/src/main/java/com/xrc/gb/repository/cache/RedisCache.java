package com.xrc.gb.repository.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * redis 缓存工具类
 * @author xu rongchao
 * @date 2020/3/5 9:36
 */
@Service
public class RedisCache {
    @Autowired
    protected RedisTemplate<Object, Object> redisTemplate;



}
