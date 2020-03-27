package com.xrc.gb.manager.go;

import com.xrc.gb.repository.cache.RedisCache;
import com.xrc.gb.repository.dao.GoDAO;
import com.xrc.gb.repository.domain.go.GoDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xu rongchao
 * @date 2020/3/23 17:02
 */
@Component
public class GoCacheManager {
    private static final String GO_KEY_PRE = "go_key_";

    private static final long GO_STORE_MINUTES = 30L;

    @Resource
    private RedisCache redisCache;

    @Resource
    private GoDAO goDAO;

    public GoDO queryGo(@NonNull Integer goId) {
        GoDO goDO = (GoDO) redisCache.get(GO_KEY_PRE + goId);
        if (goDO != null) {
            return goDO;
        } else {
            GoDO goDORep = goDAO.getById(goId);
            if (goDORep != null) {
                redisCache.set(GO_KEY_PRE + goDORep.getId(), goDORep.getGoContext(), GO_STORE_MINUTES);
                return goDORep;
            }
            return null;
        }
    }

    public boolean updateGo(@NonNull GoDO go) {
        boolean isSuccess = goDAO.update(go) == 1;
        redisCache.set(GO_KEY_PRE + go.getId(), go.getGoContext(), GO_STORE_MINUTES);
        return isSuccess;
    }

    public GoDO createGo(@NonNull GoDO go) {
        GoDO goDO = goDAO.insert(go);
        redisCache.set(GO_KEY_PRE + go.getId(), go.getGoContext(), GO_STORE_MINUTES);
        return goDO;
    }
}
