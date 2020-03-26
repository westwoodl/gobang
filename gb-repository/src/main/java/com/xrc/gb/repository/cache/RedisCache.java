package com.xrc.gb.repository.cache;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.ValueOperations;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import javax.annotation.Resource;


/**
 * redis 缓存工具类
 *
 * @author xu rongchao
 * @date 2020/3/5 9:36
 */
@Slf4j
public class RedisCache {
    protected RedisTemplate<String, Object> redisTemplate;

    public RedisCache(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 读取缓存
     */
    public Object get(final String key) {
        if (key == null) {
            return null;
        }
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 写入缓存
     */
    public boolean set(final String key, Object value) {
        if (key == null || value == null) {
            return false;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean set(final String key, Object value, Long s, TimeUnit unit) {
        if (key == null) {
            return false;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value, s, unit);
            return true;
        } catch (Exception e) {
            return false;
        }
    }




    public boolean set(final String key, Object value, Long minutes) {
        if (key == null) {
            return false;
        }
        try {
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
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
        return redisTemplate.hasKey(key);
    }
//
//    /**
//     * hset
//     */
//    public boolean hset(String key, String field, String value, long... expire) {
//        boolean result = false;
//        try {
//            final byte[] rawKey = redisTemplate.getStringSerializer().serialize(key);
//            final byte[] rawField = redisTemplate.getStringSerializer().serialize(field);
//            final byte[] rawValue = redisTemplate.getStringSerializer().serialize(value);
//            result = redisTemplate.execute(connection -> {
//                boolean ret = connection.hSet(rawKey, rawField, rawValue);
//                if (expire.length > 0 && expire[0] > 0) {
//                    connection.expire(rawKey, expire[0]);
//                }
//                return ret;
//            }, true);
//        } catch (Exception ignored) {
//        }
//        return result;
//    }
//
//    /**
//     * hget
//     */
//    public String hget(String key, String field) {
//        final byte[] rawKey = redisTemplate.getStringSerializer().serialize(key);
//        final byte[] rawField = redisTemplate.getStringSerializer().serialize(field);
//        final byte[] rawValue = redisTemplate.execute(new RedisCallback<byte[]>() {
//            public byte[] doInRedis(RedisConnection connection) {
//                return connection.hGet(rawKey, rawField);
//            }
//        }, true);
//        return redisTemplate.getStringSerializer().deserialize(rawValue);
//    }
//
//    /**
//     * hgetAll
//     */
//    public Map<String, String> hgetAll(String key) {
//        final byte[] rawKey = redisTemplate.getStringSerializer().serialize(key);
//        final Map<byte[], byte[]> rawFieldValueMap = redisTemplate.execute(new RedisCallback<Map<byte[], byte[]>>() {
//            public Map<byte[], byte[]> doInRedis(RedisConnection connection) {
//                return connection.hGetAll(rawKey);
//            }
//        }, true);
//        if (rawFieldValueMap == null) {
//            return null;
//        }
//        Map<String, String> fieldValueMap = new LinkedMap<>();
//        for (Map.Entry<byte[], byte[]> entry : rawFieldValueMap.entrySet()) {
//            fieldValueMap.put(redisTemplate.getStringSerializer().deserialize(entry.getKey()),
//                    redisTemplate.getStringSerializer().deserialize(entry.getValue()));
//        }
//        return fieldValueMap;
//    }
//

    /**
     * hIncr 原子自增
     */
    public Long hIncr(String key, String field, long... expire) {
        return hIncr(1L, key, field, expire);
    }

    /**
     * hIncr 原子自增
     * <p>
     * key
     * field
     * delta 自增的量
     */
    public Long hIncr(long delta, String key, String field, long... expire) {
        final byte[] rawKey = redisTemplate.getStringSerializer().serialize(key);
        final byte[] rawField = redisTemplate.getStringSerializer().serialize(field);
        return redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) {
                Long ret = connection.hIncrBy(rawKey, rawField, delta);
                if (expire.length > 0 && expire[0] > 0) {
                    connection.expire(rawKey, expire[0]);
                }
                return ret;
            }
        }, true);
    }

//    /**
//     * 批量删除对应的value
//     * <p>
//     * keys
//     */
//    public void remove(final String... keys) {
//        for (String key : keys) {
//            remove(key);
//        }
//    }
//
//    /**
//     * 批量删除key
//     * <p>
//     * pattern
//     */
//    public void removePattern(final String pattern) {
//        Set<String> keys = redisTemplate.keys(pattern);
//        if (keys.size() > 0) {
//            redisTemplate.delete(keys);
//        }
//    }
//
//    /**
//     * 删除对应的value
//     * <p>
//     * key
//     */
//    public void remove(final String key) {
//        if (exists(key)) {
//            redisTemplate.delete(key);
//        }
//    }
//
//    /**
//     * 判断缓存中是否有对应的value
//     * <p>
//     * key
//     */
//    public boolean exists(final String key) {
//        return redisTemplate.hasKey(key);
//    }
//
//    /**
//     * 将列表保存到缓存
//     * <p>
//     * key 缓存key
//     * expire 缓存时间
//     * data 列表数据
//     */
//    public <T> void setList2Cache(String key, long expire, List<T> data) {
//        this.set(key, JSON.toJSONString(data), expire);
//    }
//
//    /**
//     * 从缓存中获取列表
//     * <p>
//     * key 缓存key
//     * clazz
//     */
//    public <T> List<T> getListFromCache(String key, Class<T> clazz) {
//        List<T> data = new ArrayList<T>();
//        String result = this.get(key);
//        if (StringUtils.isBlank(result)) {
//            return data;
//        }
//        data = getListByJsonStr(result, clazz);
//        return data;
//    }
//
//    /**
//     * 从缓存中获取列表
//     * <p>
//     * jsonStr
//     * clazz
//     */
//    public <T> List<T> getListByJsonStr(String jsonStr, Class<T> clazz) {
//        List<T> data = new ArrayList<T>();
//        if (StringUtils.isBlank(jsonStr)) {
//            return data;
//        }
//        try {
//            JSONArray jsonArray = JSONArray.parseArray(jsonStr);
//            if (jsonArray != null && jsonArray.size() > 0) {
//                for (int i = 0; i < jsonArray.size(); i++) {
//                    T elem = JSON.parseObject(jsonArray.get(i).toString(), clazz);
//                    data.add(elem);
//                }
//            }
//        } catch (Exception ex) {
//        }
//
//        return data;
//    }
//
//    /**
//     * 将对象保存到缓存
//     * <p>
//     * key 缓存key
//     * expire 缓存时间
//     * object 对象
//     */
//    public <T> void setObject2Cache(String key, long expire, T object) {
//        this.set(key, JSON.toJSONString(object), expire);
//    }
//
//    /**
//     * 从缓存中获取对象
//     * <p>
//     * key 缓存key
//     * clazz
//     */
//    public <T> T getObjectFromCache(String key, Class<T> clazz) {
//        String result = get(key);
//        if (StringUtils.isBlank(result)) {
//            return null;
//        }
//        T elem = null;
//        try {
//            elem = JSON.parseObject(result, clazz);
//        } catch (Exception ex) {
//        }
//        return elem;
//    }
//
//    public interface GetData<T> {
//        T get();
//    }
//
//    public interface GetListData<T> {
//        List<T> get();
//    }
//
//    /**
//     * 把一个对象缓存到hash中
//     * <p>
//     * key
//     * obj
//     * expire
//     */
//    public Boolean hsetObj(String key, Object obj, long expire) {
//        HashMap parseObject = JSON.parseObject(JSON.toJSONString(obj), HashMap.class);
//        Map<byte[], byte[]> byteMap = new HashMap<byte[], byte[]>();
//        Set<Entry> entrySet = parseObject.entrySet();
//        for (Entry entry : entrySet) {
//            byte[] keyBytes = redisTemplate.getStringSerializer().serialize(entry.getKey().toString());
//            byte[] valueBytes = redisTemplate.getStringSerializer().serialize(entry.getValue().toString());
//            byteMap.put(keyBytes, valueBytes);
//        }
//        final byte[] rawKey = redisTemplate.getStringSerializer().serialize(key);
//        return redisTemplate.execute(new RedisCallback<Boolean>() {
//            public Boolean doInRedis(RedisConnection connection) {
//                connection.hMSet(rawKey, byteMap);
//                if (expire > 0) {
//                    connection.expire(rawKey, expire);
//                }
//                return true;
//            }
//        }, true);
//    }
//
//    /**
//     * 在一个hash中获得一个对象
//     * <p>
//     * key
//     * clazzType
//     */
//    public <T> T hgetObj(String key, Class<T> clazzType) {
//        final byte[] rawKey = redisTemplate.getStringSerializer().serialize(key);
//        final Map<byte[], byte[]> byteMap = redisTemplate.execute(new RedisCallback<Map<byte[], byte[]>>() {
//            public Map<byte[], byte[]> doInRedis(RedisConnection connection) {
//                return connection.hGetAll(rawKey);
//            }
//        }, true);
//        if (byteMap == null) {
//            return null;
//        }
//        if (byteMap.isEmpty()) {
//            return null;
//        }
//        Set<Entry<byte[], byte[]>> entrySet = byteMap.entrySet();
//        Map<String, String> resMap = new HashMap<String, String>();
//        for (Entry<byte[], byte[]> entry : entrySet) {
//            String fKey = redisTemplate.getStringSerializer().deserialize(entry.getKey());
//            String fValue = redisTemplate.getStringSerializer().deserialize(entry.getValue());
//            resMap.put(fKey, fValue);
//        }
//        T t = null;
//        try {
//            t = JSON.parseObject(JSON.toJSONString(resMap), clazzType);
//        } catch (Exception ex) {
//        }
//        return t;
//    }
//
//    /**
//     * hash原子递减
//     * <p>
//     * key
//     * field
//     * expire
//     */
//    public void hDec(String key, String field, long expire) {
//        hIncr(-1L, key, field, expire);
//    }
//
//    public <T> void rpush(String key, long expore, List<T> collection) {
//        Collection<String> collect =
//                collection.stream().map(item -> JSONObject.toJSONString(item)).collect(Collectors.toList());
//        redisTemplate.opsForList().rightPushAll(key, collect);
//        if (expore > 0) {
//            redisTemplate.expire(key, expore, TimeUnit.SECONDS);
//        }
//    }
//
//    public void lpush(String key, long livetime, Object obj) {
//        redisTemplate.opsForList().rightPush(key, JSONObject.toJSONString(obj));
//        expireKey(key, livetime);
//    }
//
//    public <T> List<T> lrange(String key, int start, int size, Class<T> clazz) {
//        List<T> resultList = new ArrayList<>();
//        // 底层默认用的lrange,这一点需要注意
//        try {
//            List<String> rangeList = redisTemplate.opsForList().range(key, start, start + size);
//            if (rangeList == null) {
//                return resultList;
//            }
//            return rangeList.stream().map(jsonStr -> JSONObject.parseObject(jsonStr, clazz)).filter(temObj -> temObj != null)
//                    .collect(Collectors.toList());
//        } catch (Exception ex) {
//            return resultList;
//        }
//    }
//
//    public <T> List<T> rangeAll(String key, Class<T> clazz) {
//        List<T> resultList = new ArrayList<>();
//        // 底层默认用的lrange,这一点需要注意
//        try {
//            List<String> rangeList = redisTemplate.opsForList().range(key, 0, -1);
//            if (rangeList == null) {
//                return resultList;
//            }
//            return rangeList.stream().map(jsonStr -> JSONObject.parseObject(jsonStr, clazz)).filter(temObj -> temObj != null)
//                    .collect(Collectors.toList());
//        } catch (Exception ex) {
//            return resultList;
//        }
//    }
//
//    public void zadd(String key, Object value, double score, long liveTime) {
//        redisTemplate.opsForZSet().add(key, JSONObject.toJSONString(value), score);
//        expireKey(key, liveTime);
//    }
//
//    public <T> List<T> zrange(String key, int start, int size, Class<T> clazz) {
//        Set<String> range = redisTemplate.opsForZSet().range(key, start, start + size - 1);
//        return range.stream().map(item -> JSONObject.parseObject(item, clazz)).collect(Collectors.toList());
//    }
//
//    public <T> List<T> zreverseRange(String key, int start, int size, Class<T> clazz) {
//        Set<String> range = redisTemplate.opsForZSet().reverseRange(key, start, start + size - 1);
//        return range.stream().map(item -> JSONObject.parseObject(item, clazz)).collect(Collectors.toList());
//    }
//
//    public void expireKey(String key, long livetime) {
//        redisTemplate.expire(key, livetime, TimeUnit.SECONDS.SECONDS);
//    }
//
//    public long incr(String key) {
//        return incr(key, 1);
//    }
//
//    public long incr(String key, long delta) {
//        Long increment = redisTemplate.opsForValue().increment(key, delta);
//        return increment;
//    }
//
//    public long incr(String key, long delta, long liveTime) {
//        long incr = incr(key, delta);
//        if (incr == 1) {
//            expireKey(key, liveTime);
//        }
//        return incr;
//    }
//
//    public Set<String> keys(String pattern) {
//        return redisTemplate.keys(pattern);
//    }
//
//    public void removeKes(Set<String> keys) {
//        redisTemplate.delete(keys);
//    }


}
