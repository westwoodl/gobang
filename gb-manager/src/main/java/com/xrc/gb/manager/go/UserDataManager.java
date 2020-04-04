package com.xrc.gb.manager.go;

import com.xrc.gb.manager.AbstractCacheManager;
import com.xrc.gb.repository.dao.UserDAO;
import com.xrc.gb.repository.domain.user.UserDO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xu rongchao
 * @date 2020/4/3 22:08
 */
@Component("userDataManager")
public class UserDataManager extends AbstractCacheManager<UserDO> {
    @Resource
    private UserDAO userDAO;

    @Override
    public boolean insert(UserDO domain) {
        if (userDAO.insert(domain) > 0) {
            setCache(userDAO.queryById(domain.getId()));
            return true;
        }
        return false;
    }

    @Override
    public UserDO queryById(int id) {
        UserDO userDO = getCache(id);
        if (userDO != null) {
            return userDO;
        }
        userDO = userDAO.queryById(id);
        if (userDO == null) {
            setNullCache(id);
            return null;
        }
        setCache(userDO);
        return userDO;
    }

    @Override
    public boolean update(UserDO domain) {
        if (userDAO.update(domain) > 0) {
            setCache(userDAO.queryById(domain.getId()));
            return true;
        }
        return false;
    }
}
