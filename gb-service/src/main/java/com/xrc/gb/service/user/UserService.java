package com.xrc.gb.service.user;

import com.xrc.gb.repository.domain.user.UserDO;

/**
 * @author xu rongchao
 * @date 2020/3/4 15:55
 */
public interface UserService {
    boolean add(String username, String account, String password);

    UserDO find(Integer id);

    UserDO login(String account, String pwd);
}
