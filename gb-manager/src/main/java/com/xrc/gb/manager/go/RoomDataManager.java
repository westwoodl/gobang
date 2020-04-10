package com.xrc.gb.manager.go;

import com.xrc.gb.common.consts.CommonConst;
import com.xrc.gb.repository.cache.CacheKeyUtils;
import com.xrc.gb.repository.cache.TypeRedisCache;
import com.xrc.gb.repository.dao.RoomDAO;
import com.xrc.gb.repository.domain.go.RoomDO;
import com.xrc.gb.common.util.PageQueryReq;
import com.xrc.gb.common.util.PageQueryResultResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
// 查询常有，而修改不常有，

/**
 * 查询常有，而修改不常有，
 * 将房间id保存在一个list里面
 *
 * @author xu rongchao
 * @date 2020/3/28 13:47
 */
@Component
@Transactional(rollbackFor = {Exception.class})
@Slf4j
public class RoomDataManager {

    @Resource
    private RoomDAO roomDAO;

    @Resource
    private TypeRedisCache<RoomDO> roomTypeRedisCache;

    public final static String ROOM_CACHE_KEY_PRE = "room_key_";

    /**
     * 实时性问题非常严重
     * @param pageQuery
     * @return
     */
//    @Deprecated
//    public PageQueryResultResp<List<RoomDO>> queryPageCache(@NonNull final PageQueryReq<RoomDO> pageQuery) {
//        PageQueryResultResp<List<RoomDO>> cacheResp = pageTypeRedisCache.get(CacheKeyUtils.getPageKey(pageQuery));
//        if (cacheResp != null && cacheResp.getTotalCount() > 0) {
//            return cacheResp;
//        }
//        int daoCount = roomDAO.countAll(pageQuery.getData());
//        PageQueryResultResp<List<RoomDO>> pageQueryResultResp = pageQuery.getQueryResult(daoCount);
//        if (daoCount > 0) {
//            pageQueryResultResp.getData().addAll(roomDAO.queryAllByLimit(pageQuery.getData(), pageQuery.getOffSet(), pageQuery.getPageSize()));
//            pageTypeRedisCache.set(CacheKeyUtils.getPageKey(pageQuery), pageQueryResultResp, CommonConst.CACHE_STORE_MINUTES);
//            log.info("缓存击穿查询条件：" + pageQuery);
//            return pageQueryResultResp;
//        }
//        pageTypeRedisCache.set(CacheKeyUtils.getPageKey(pageQuery), pageQueryResultResp, CommonConst.NULL_CACHE_STORE_MINUTES);
//        return pageQueryResultResp;
//    }

    public PageQueryResultResp<List<RoomDO>> queryPage(@NonNull final PageQueryReq<RoomDO> pageQuery) {
        int daoCount = roomDAO.countAll(pageQuery.getData());
        PageQueryResultResp<List<RoomDO>> pageQueryResultResp = pageQuery.getQueryResult(daoCount);
        if (daoCount > 0) {
            pageQueryResultResp.getData().addAll(roomDAO.queryAllByLimit(pageQuery.getData(), pageQuery.getOffSet(), pageQuery.getPageSize()));
            return pageQueryResultResp;
        }
        return pageQueryResultResp;
    }

    public boolean createRoom(@NonNull RoomDO roomDO) {
        if (roomDAO.insert(roomDO) > 0) {
            setCache(roomDAO.queryById(roomDO.getId()));
            return true;
        }
        return false;
    }

    public boolean deleteById(int roomId) {
        roomTypeRedisCache.remove(CacheKeyUtils.getIdKey(roomId, RoomDO.class));
        if(roomDAO.deleteById(roomId) > 0) {
            roomTypeRedisCache.remove(CacheKeyUtils.getIdKey(roomId, RoomDO.class));
            return true;
        }
        return false;
        // 删除失败 什么操蛋情况会删除失败？
        // RoomDO roomDO = roomDAO.queryById(roomId);
        //roomTypeRedisCache.set(CacheKeyUtils.getIdKey(roomDO), roomDO, CommonConst.CACHE_STORE_MINUTES);
    }

    public boolean update(@NonNull RoomDO roomDO) {
        if (roomDAO.update(roomDO) > 0) {
            setCache(roomDAO.queryById(roomDO.getId()));
            return true;
        }
        return false;
    }

    public RoomDO queryById(@NonNull int roomId) {
        RoomDO roomDO = getCache(roomId);
        if (roomDO != null && roomDO.getId() != null && roomDO.getId().equals(roomId)) {
            return roomDO;
        }
        RoomDO daoRoomDO = roomDAO.queryById(roomId);
        if (daoRoomDO == null) {
            log.info("RoomDo缓存穿透：{}", roomId);
            setNullCache(roomId);
            return null;
        }
        log.info("RoomDO缓存击穿" + daoRoomDO);
        setCache(daoRoomDO);
        return daoRoomDO;
    }

    private void setCache(RoomDO roomDO) {
        roomTypeRedisCache.set(CacheKeyUtils.getIdKey(roomDO), roomDO, CommonConst.CACHE_STORE_MINUTES);
    }

    private void setNullCache(Integer roomId) {
        roomTypeRedisCache.set(CacheKeyUtils.getIdKey(roomId, RoomDO.class), null, CommonConst.NULL_CACHE_STORE_MINUTES);
    }

    private RoomDO getCache(Integer roomId) {
        return roomTypeRedisCache.get(CacheKeyUtils.getIdKey(roomId, RoomDO.class));
    }
}
