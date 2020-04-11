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
public class UserDataManager extends AbstractCacheManager<UserDO, UserDAO> {

    @Override
    public boolean deleteById(int id) {
        throw new UnsupportedOperationException();
    }
}
