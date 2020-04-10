package com.xrc.gb.repository.cache;

import com.xrc.gb.common.util.ExceptionHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author xu rongchao
 * @date 2020/3/29 9:11
 */
@Component
@Slf4j
public class TypeRedisCache<T> {

    @Resource
    private RedisTemplate<String, T> redisTemplate;

    /**
     * 读取缓存
     */
    public T get(final String key) {
        if (key == null) {
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 写入缓存
     */
    public boolean set(final String key, T value) {
        if (key == null || value == null) {
            return false;
        }
        ValueOperations<String, T> operations = redisTemplate.opsForValue();
        operations.set(key, value);
        return true;
    }

    public boolean set(final String key, T value, Long minutes) {
        if (key == null) {
            return false;
        }
        try {
            ValueOperations<String, T> operations = redisTemplate.opsForValue();
            operations.set(key, value, minutes, TimeUnit.MINUTES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除对应的value
     * <p>
     * key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }


    /**
     * 判断缓存中是否有对应的value
     * <p>
     * key
     */
    public boolean exists(final String key) {
        if (key == null) {
            return false;
        }
        Boolean exists = redisTemplate.hasKey(key);
        return exists == null ? false : exists;
    }


    @Deprecated
    public boolean zSetAdd(final String key, final T value, final double score) {
        return boo(redisTemplate.opsForZSet().add(key, value, score));
    }

    @Deprecated
    public T zSetGet(String key, double score) {
        Set<T> set = redisTemplate.opsForZSet().rangeByScore(key, score, score);
        if (set == null) {
            return null;
        }
        for (T t : set) {
            if (set.size() > 1) {
                throw ExceptionHelper.newSysException("redis的room出现重复");
            }
            return t;
        }
        return null;
    }

    @Deprecated
    public int zSetCount(final String key) {
        return longToInt(redisTemplate.opsForZSet().size(key));
    }

    @Deprecated
    public Set<T> zSetRang(final String key, int start, int stop) {
        return redisTemplate.opsForZSet().range(key, start, stop);
    }

    /**
     * [start, stop]
     */
    @Deprecated
    public Set<T> zSetReverseRange(final String key, int start, int stop) {
        return redisTemplate.opsForZSet().reverseRange(key, start, stop);
    }

    /**
     * [s1, s2]
     */
    @Deprecated
    public int zSetRemoveRangeByScore(final String key, double score1, double score2) {
        return longToInt(redisTemplate.opsForZSet().removeRangeByScore(key, score1, score2));
    }


    private boolean boo(Boolean b) {
        return b == null ? false : b;
    }

    private int longToInt(Long l) {
        return l == null ? 0 : (int) (long) l;
    }

}
