package com.xrc.gb.manager.go;

import com.xrc.gb.manager.AbstractCacheManager;
import com.xrc.gb.repository.cache.TypeRedisCache;
import com.xrc.gb.repository.dao.UserDAO;
import com.xrc.gb.repository.domain.user.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xu rongchao
 * @date 2020/4/3 22:08
 */
@Component("userDataManager")
public class UserDataManager extends AbstractCacheManager<UserDO> {
    @Resource
    private TypeRedisCache<UserDO> tTypeRedisCache;
    @Resource
    private UserDAO userDAO;

    @Override
    public int insert(UserDO domain) {
        return 0;
    }

    @Override
    public UserDO queryById(int id) {
        return null;
    }

    @Override
    public int update(UserDO domain) {
        return 0;
    }

}
