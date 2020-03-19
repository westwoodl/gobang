package com.xrc.gb.repository.dao;

import com.xrc.gb.repository.domain.BaseDO;

/**
 * @author xu rongchao
 * @date 2020/3/4 10:35
 */
public interface BaseDAO<T extends BaseDO> {

    T insert(T domain);

    T getById(int id);

    int delete(int id);

    int update(T domain);

}
