package com.xrc.gb.manager.go;

import com.xrc.gb.common.consts.CommonConst;
import com.xrc.gb.manager.AbstractCacheManager;
import com.xrc.gb.repository.cache.CacheKeyUtils;
import com.xrc.gb.repository.cache.TypeRedisCache;
import com.xrc.gb.repository.dao.GoDAO;
import com.xrc.gb.repository.domain.go.GoDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author xu rongchao
 * @date 2020/3/23 17:02
 */
@Component
@Transactional(rollbackFor = {Exception.class})
@Slf4j
public class GoDataManager extends AbstractCacheManager<GoDO, GoDAO> {
    // 这...
}
