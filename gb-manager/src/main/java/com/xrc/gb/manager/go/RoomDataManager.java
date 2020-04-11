package com.xrc.gb.manager.go;

import com.xrc.gb.common.consts.CommonConst;
import com.xrc.gb.manager.AbstractCacheManager;
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
public class RoomDataManager extends AbstractCacheManager<RoomDO, RoomDAO> {


    public PageQueryResultResp<List<RoomDO>> queryPage(@NonNull final PageQueryReq<RoomDO> pageQuery) {
        int daoCount = baseDAO.countAll(pageQuery.getData());
        PageQueryResultResp<List<RoomDO>> pageQueryResultResp = pageQuery.getQueryResult(daoCount);
        if (daoCount > 0) {
            pageQueryResultResp.getData().addAll(baseDAO.queryAllByLimit(pageQuery.getData(), pageQuery.getOffSet(), pageQuery.getPageSize()));
            return pageQueryResultResp;
        }
        return pageQueryResultResp;
    }


}
