package com.xrc.gb.manager.go;

import com.xrc.gb.repository.cache.RedisCache;
import com.xrc.gb.repository.cache.TypeRedisCache;
import com.xrc.gb.repository.dao.RoomDAO;
import com.xrc.gb.repository.domain.go.RoomDO;
import com.xrc.gb.util.PageQueryReq;
import com.xrc.gb.util.PageQueryResultResp;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
// 查询常有，而修改不常有，

/**
 * 查询常有，而修改不常有，
 * 将房间id保存在一个list里面
 *
 * @author xu rongchao
 * @date 2020/3/28 13:47
 */
@Component
@Transactional
public class RoomDataManager {

    @Resource
    private RoomDAO roomDAO;

    @Resource
    private TypeRedisCache<RoomDO> roomTypeRedisCache;

    public final static String ROOM_CACHE_KEY_PRE = "room_key_";

    /**
     * 保存的是room_id
     */
    public final static String ROOM_SET_CACHE_KEY = "room_set_key";

    public PageQueryResultResp<List<RoomDO>> queryPage(@NonNull final PageQueryReq<RoomDO> pageQuery) {
        int start = pageQuery.getStart();
        int stop = pageQuery.getRedisStop();
        long totalCount = roomTypeRedisCache.zSetCount(ROOM_SET_CACHE_KEY);
        List<RoomDO> roomDOList = new ArrayList<>();
        PageQueryResultResp<List<RoomDO>> resultResp = new PageQueryResultResp<>();
        resultResp.setData(roomDOList);
        resultResp.setPageIndex(pageQuery.getPageIndex());
        resultResp.setPageSize(pageQuery.getPageIndex());

        if (totalCount > 0) {
            resultResp.setTotalCount((int) totalCount);
            roomDOList.addAll(roomTypeRedisCache.zSetRang(ROOM_SET_CACHE_KEY, start, stop));
        } else {
            int dao_count = roomDAO.countAll(pageQuery.getData());
            resultResp.setTotalCount(dao_count);
            if (dao_count > 0) {
                roomDOList.addAll(roomDAO.queryAllByLimit(pageQuery.getData(), start, pageQuery.getPageSize()));
                if (CollectionUtils.isNotEmpty(roomDOList)) {
                    roomTypeRedisCache.remove(ROOM_SET_CACHE_KEY);
                    for (RoomDO roomDO : roomDOList) {
                        roomTypeRedisCache.zSetAdd(ROOM_SET_CACHE_KEY, roomDO, roomDO.getId());
                    }
                }
            }
        }
        return resultResp;
    }

    public boolean createRoom(@NonNull RoomDO roomDO) {
        if (roomDAO.insert(roomDO) > 0) {
            roomTypeRedisCache.zSetAdd(ROOM_SET_CACHE_KEY, roomDO, roomDO.getId());
            return true;
        }
        return false;
    }

    public boolean deleteById(@NonNull int roomId) {
        if (roomDAO.deleteById(roomId) > 0) {
            roomTypeRedisCache.zSetRemoveRangeByScore(ROOM_SET_CACHE_KEY, roomId, roomId);
            return true;
        }
        return false;
    }

    public boolean update(@NonNull RoomDO roomDO) {
        if (roomDAO.update(roomDO) > 0) {
            roomTypeRedisCache.zSetAdd(ROOM_SET_CACHE_KEY, roomDO, roomDO.getId());
        }
        return false;
    }
}
