package com.xrc.gb.repository;

import com.xrc.gb.repository.cache.RedisCache;
import com.xrc.gb.repository.cache.TypeRedisCache;
import com.xrc.gb.repository.domain.go.GoDO;
import com.xrc.gb.repository.domain.go.RoomDO;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Set;

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
     * 这两个的地址是一样的
     */
    @Resource
    TypeRedisCache<RoomDO> typeRedisCache;
    @Resource
    TypeRedisCache<GoDO> typeRedisCache2;
    @Resource
    TypeRedisCache<String> stringTypeRedisCache;

    /**
     * StringRedisTemplate默认选择的StringRedisSerializer序列化器
     */
    @Test
    public void test_redis() {
        redisTemplate.opsForValue().set("xrc", "myValue");
        System.out.println(redisTemplate.opsForValue().get("xrc") + "=====");
    }


    @Test
    public void test_redis_obj() {
        RoomDO roomDO = new RoomDO();
        roomDO.setRoomPassword("12345678");
        roomDO.setRoomName("xrc");
        redisTemplate.opsForValue().set("xrc", roomDO);
    }

    @Test
    public void test_redis_obj_query() {
        RoomDO roomDO = (RoomDO) redisTemplate.opsForValue().get("xrc");
        System.out.println(roomDO.getRoomPassword());
        System.out.println(roomDO.getRoomName());
    }
    public final static String ROOM_SET_CACHE_KEY = "room_set_key";

    @Test
    public void test_redis_room() {
//        RoomDO roomDO = new RoomDO();
//        roomDO.setRoomPassword("12345678");
//        roomDO.setRoomName("xrc");
        System.out.println(typeRedisCache.zSetRang(ROOM_SET_CACHE_KEY, 0, 6));
        System.out.println(typeRedisCache.zSetGet(ROOM_SET_CACHE_KEY, 11));
        typeRedisCache.zSetRemoveRangeByScore(ROOM_SET_CACHE_KEY, 16, 16);
        System.out.println(typeRedisCache.zSetRang(ROOM_SET_CACHE_KEY, 0, 6));


        GoDO goDO = new GoDO();
    }


    @Test
    public void type_redis() {
        RoomDO roomDO = new RoomDO();
        roomDO.setRoomPassword("12345678");
        roomDO.setRoomName("xrc");
        typeRedisCache.set("xrc", roomDO);

        GoDO goDO = new GoDO();
        goDO.setId(123);

        typeRedisCache2.set("xrc2", goDO);

        System.out.println(typeRedisCache.get("xrc").getRoomPassword());
        System.out.println(typeRedisCache.get("xrc").getRoomName());

        System.out.println(typeRedisCache2.get("xrc2").getId());
        System.out.println(typeRedisCache2.equals(typeRedisCache));


    }


    @Test
    public void test_set() {
//        typeRedisCache.remove("sset");
//        typeRedisCache.zSetAdd("sset", new RoomDO(1, "1"), 1.0);
//        typeRedisCache.zSetAdd("sset", new RoomDO(1, "1"), 1.1);
//        typeRedisCache.zSetAdd("sset", new RoomDO(1, "1"), 1.1);
//        typeRedisCache.zSetAdd("sset", new RoomDO(2, "1"), 1.2);
//
//        System.out.println(typeRedisCache.zSetRang("sset", 0, 2));
//        System.out.println(typeRedisCache.zSetRang("sset", 1, 2));
//        Set<RoomDO> set = typeRedisCache.zSetRang("sset", 0, 5);
//        for (RoomDO s : set) {
//            System.out.println(s);
//        }

        RoomDO roomDO = new RoomDO();
        roomDO.setId(1);
        roomDO.setRoomName("ssa");
        typeRedisCache.zSetAdd("sd", roomDO, roomDO.getId());
        System.out.println(typeRedisCache.zSetRemoveRangeByScore("sset", 0, 1.3));
        System.out.println(typeRedisCache.zSetReverseRange("sset", 0, 5));
    }


}
