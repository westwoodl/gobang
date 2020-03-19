package com.xrc.gb.service.user;

import com.xrc.gb.repository.dao.UserDAO;
import com.xrc.gb.repository.domain.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xu rongchao
 * @date 2020/3/4 15:35
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDAO userDAO;

    public boolean add(String username, String account, String password) {

        UserDO userDO = new UserDO();
        userDO.setUserName(username);
        userDO.setAccount(account);
        userDO.setPassword(password);
        UserDO re = userDAO.insert(userDO);
        return true;
    }

    @Override
    public UserDO find(Integer id) {
        return userDAO.getById(id);
    }
}
