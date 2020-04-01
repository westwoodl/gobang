package com.xrc.gb.repository.dao;

import com.xrc.gb.repository.domain.user.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author xu rongchao
 * @date 2020/3/4 10:29
 */
@Mapper
public interface UserDAO extends BaseDAO<UserDO> {

    int insertSelective(UserDO userDO);

    int updateByPrimaryKeySelective(UserDO userDO);

    UserDO queryByAccountAndPwd(@Param("account") String account, @Param("pwd") String pwd);

    UserDO queryByUserName(String username);

    UserDO queryByAccount(String account);
}
