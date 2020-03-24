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
public class GoManager {
    private static final String GO_KEY_PRE = "go_key_";

    @Resource
    private RedisCache redisCache;

    @Resource
    private GoDAO goDAO;

    public GoDO queryGo(@NonNull Integer goId) {
        String value = redisCache.get(GO_KEY_PRE + goId);
        if (StringUtils.isNotBlank(value)) {
            GoDO goDO = new GoDO();
            goDO.setGoContext(value);
            return goDO;
        }
        return goDAO.getById(goId);
    }

    public boolean updateGo(@NonNull GoDO go) {
        boolean isSuccess = redisCache.set(GO_KEY_PRE + go.getId(), go.getGoContext(), 30L);
        if (isSuccess) {
            return goDAO.insert(go) != null;
        }
        return false;
    }

    public GoDO createGo(@NonNull GoDO go) {
        return new GoDO();
    }
}
