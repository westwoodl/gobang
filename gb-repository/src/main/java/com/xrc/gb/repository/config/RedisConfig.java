package com.xrc.gb.repository.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.xrc.gb.repository.cache.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author xu rongchao
 * @date 2020/3/4 10:01
 */
@Configuration
@Slf4j
public class RedisConfig {

    @Bean("redisCache")
    public RedisCache redisCache(RedisTemplate<String, Object> redisTemplate) {
        return new RedisCache(redisTemplate);
    }

    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericFastJsonRedisSerializer());
//        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return template;
    }
}
