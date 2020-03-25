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

    private static final long GO_STORE_MINUTES =  30L;

    @Resource
    private RedisCache redisCache;

    @Resource
    private GoDAO goDAO;

    public GoDO queryGo(@NonNull Integer goId) {
        String value = String.valueOf(redisCache.get(GO_KEY_PRE + goId));
        if (StringUtils.isNotBlank(value)) {
            GoDO goDO = new GoDO();
            goDO.setGoContext(value);
            return goDO;
        } else {
            GoDO goDO = goDAO.getById(goId);
            if(goDO!=null) {
                redisCache.set(GO_KEY_PRE + goDO.getId(), goDO.getGoContext(), GO_STORE_MINUTES);
                return goDO;
            }
            return null;
        }
    }

    public boolean updateGo(@NonNull GoDO go) {
        boolean isSuccess = redisCache.set(GO_KEY_PRE + go.getId(), go.getGoContext(), GO_STORE_MINUTES);
        if (isSuccess) {
            return goDAO.insert(go) != null;
        }
        return false;
    }

    public GoDO createGo(@NonNull GoDO go) {
        return new GoDO();
    }
}
