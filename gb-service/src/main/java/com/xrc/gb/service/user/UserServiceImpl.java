package com.xrc.gb.service.user;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.consts.ErrorInfoConstants;
import com.xrc.gb.manager.go.UserDataManager;
import com.xrc.gb.repository.dao.UserDAO;
import com.xrc.gb.repository.domain.user.UserDO;
import com.xrc.gb.util.CheckParameter;
import com.xrc.gb.util.ExceptionHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xu rongchao
 * @date 2020/3/4 15:35
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDAO userDAO;

    @Resource
    private UserDataManager userDataManager;

    @Override
    public boolean add(String username, String account, String password) {
        CheckParameter.assertTrue(StringUtils.isNotBlank(username));
        CheckParameter.assertTrue(StringUtils.isNotBlank(account));
        CheckParameter.assertTrue(StringUtils.isNotBlank(password));
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
        userDO.setImg("https://s1.ax1x.com/2020/04/06/GsWKDH.jpg");
        return userDataManager.insert(userDO);
    }

    @Override
    public UserDO find(Integer id) {
        CheckParameter.isNotNull(id);
        return userDataManager.queryById(id);
    }

    @Override
    public UserDO login(String account, String pwd) {
        return userDAO.queryByAccountAndPwd(account, pwd);
    }

    @Override
    public boolean update(UserDO userDO) {
        return userDataManager.update(userDO);
    }

    @Override
    public boolean addFriend(int friendId, Integer userId) {
        CheckParameter.isNotNull(userId, ErrorInfoConstants.BIZ_PLEASE_LOGIN);
        UserDO friendUserDO = userDataManager.queryById(friendId);
        if (friendUserDO==null) {
            throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_USER_NOT_EXIST);
        }

        UserDO userDO = userDataManager.queryById(userId);
        List<Integer> friendList = JSONObject.parseArray(userDO.getFriend(), Integer.class);
        if (CollectionUtils.isEmpty(friendList)) {
            friendList = new ArrayList<>();
        }
        friendList.add(friendUserDO.getId());

        UserDO userUpdateDO = new UserDO();
        userUpdateDO.setId(userId);
        userUpdateDO.setFriend(JSONObject.toJSONString(friendList));
        return userDataManager.update(userUpdateDO);
    }


}
