package com.xrc.gb.repository.dao;

import com.xrc.gb.repository.domain.UserDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author xu rongchao
 * @date 2020/3/4 10:29
 */
@Mapper
public interface UserDAO extends BaseDAO<UserDO> {

    int insertSelective(UserDO userDO);

    int updateByPrimaryKeySelective(UserDO userDO);
}
