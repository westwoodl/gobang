package com.xrc.gb.manager.go;

import com.xrc.gb.consts.CommonConst;
import com.xrc.gb.manager.AbstractCacheManager;
import com.xrc.gb.repository.cache.CacheKeyUtils;
import com.xrc.gb.repository.cache.RedisCache;
import com.xrc.gb.repository.cache.TypeRedisCache;
import com.xrc.gb.repository.dao.GoDAO;
import com.xrc.gb.repository.domain.go.GoDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author xu rongchao
 * @date 2020/3/23 17:02
 */
@Component
@Transactional
@Slf4j
public class GoDataManager {

    @Resource
    private TypeRedisCache<GoDO> goTypeRedisCache;

    @Resource
    private GoDAO goDAO;

    public GoDO queryGo(@NonNull Integer goId) {
        GoDO goDO = getCache(goId);
        if (goDO != null) {
            return goDO;
        } else {
            GoDO goDORep = goDAO.queryById(goId);
            if (goDORep != null) {
                log.info("go缓存击穿id{}", goId);
                setCache(goDORep);
                return goDORep;
            }
            log.info("go缓存穿透id{}", goId);
            // 设置空值防止缓存穿透
            setNullCache(goId);
            return null;
        }
    }

    public boolean updateGo(@NonNull GoDO go) {
        boolean isSuccess = goDAO.update(go) == 1;
        if (isSuccess) {
            setCache(goDAO.queryById(go.getId()));
            return true;
        }
        return false;
    }

    public boolean createGo(@NonNull GoDO go) {
        if (goDAO.insert(go) > 0) {
            setCache(goDAO.queryById(go.getId()));
            return true;
        }
        return false;
    }

    private void setCache(GoDO go) {
        goTypeRedisCache.set(CacheKeyUtils.getIdKey(go), go, CommonConst.CACHE_STORE_MINUTES);
    }

    private void setNullCache(Integer goId) {
        goTypeRedisCache.set(CacheKeyUtils.getIdKey(goId, GoDO.class), null, CommonConst.NULL_CACHE_STORE_MINUTES);
    }

    private GoDO getCache(Integer goId) {
        return goTypeRedisCache.get(CacheKeyUtils.getIdKey(goId, GoDO.class));
    }
}
