package com.xrc.gb.repository.dao;

import com.xrc.gb.repository.domain.BaseDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xu rongchao
 * @date 2020/3/4 10:35
 */
public interface BaseDAO<T extends BaseDO> {

    int insert(T domain);

    T queryById(int id);

    List<T> queryAllByLimit(@Param("t") T t, @Param("offset")Integer offset, @Param("limit") Integer limit);

    int countAll(@Param("t") T t);

    int deleteById(int id);

    int update(T domain);

}
