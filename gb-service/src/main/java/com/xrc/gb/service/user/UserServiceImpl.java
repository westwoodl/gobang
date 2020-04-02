package com.xrc.gb.service.user;

import com.xrc.gb.repository.dao.UserDAO;
import com.xrc.gb.repository.domain.user.UserDO;
import com.xrc.gb.util.CheckParameter;
import com.xrc.gb.util.ExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xu rongchao
 * @date 2020/3/4 15:35
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDAO userDAO;

    public boolean add(String username, String account, String password) {
        if (userDAO.queryByUserName(username) != null) {
            throw ExceptionHelper.newBusinessException("昵称已存在");
        }
        if (userDAO.queryByUserName(account) != null) {
            throw ExceptionHelper.newBusinessException("账号已存在");
        }

        UserDO userDO = new UserDO();
        userDO.setUserName(username);
        userDO.setAccount(account);
        userDO.setPassword(password);
        int id = userDAO.insert(userDO);
        return id > 0;
    }

    @Override
    public UserDO find(Integer id) {
        CheckParameter.isNotNull(id);
        return userDAO.queryById(id);
    }

    @Override
    public UserDO login(String account, String pwd) {
        return userDAO.queryByAccountAndPwd(account, pwd);
    }

    @Override
    public boolean update(UserDO userDO) {
        return userDAO.update(userDO) == 1;
    }


}
