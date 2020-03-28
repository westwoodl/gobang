package com.xrc.gb.service.user;

import com.xrc.gb.repository.dao.UserDAO;
import com.xrc.gb.repository.domain.user.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xu rongchao
 * @date 2020/3/4 15:35
 */
@Service
public class UserServiceImpl implements UserService{

    @Resource
    private UserDAO userDAO;

    public boolean add(String username, String account, String password) {

        UserDO userDO = new UserDO();
        userDO.setUserName(username);
        userDO.setAccount(account);
        userDO.setPassword(password);
        int id = userDAO.insert(userDO);
        return id > 0;
    }

    @Override
    public UserDO find(Integer id) {
        return userDAO.queryById(id);
    }
}
