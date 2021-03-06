package com.xrc.gb.manager;

import com.xrc.gb.common.consts.CommonConst;
import com.xrc.gb.repository.cache.CacheKeyUtils;
import com.xrc.gb.repository.cache.TypeRedisCache;
import com.xrc.gb.repository.dao.BaseDAO;
import com.xrc.gb.repository.dao.RoomDAO;
import com.xrc.gb.repository.domain.BaseDO;
import com.xrc.gb.repository.domain.go.RoomDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import java.lang.reflect.ParameterizedType;

/**
 * 使用
 *
 * @author xu rongchao
 * @date 2020/4/3 20:59
 */
@Slf4j
public abstract class AbstractCacheManager<T extends BaseDO, D extends BaseDAO<T>> {

    /**
     * 在Spring Boot2.0中可以直接在父类属性上加入注解
     */
    @Autowired
    protected TypeRedisCache<T> tTypeRedisCache;

    @Autowired
    protected D baseDAO;
    /**
     * 1. public Type getGenericSuperclass()
     * 用来返回表示当前Class 所表示的实体（类、接口、基本类型或 void）的直接超类的Type。如果这个直接超类是参数化类型的，
     * 则返回的Type对象必须明确反映在源代码中声明时使用的类型
     * <p>
     * 2. public Type[] getGenericInterfaces()
     * 与上面那个方法类似，只不过Java的类可以实现多个接口，所以返回的Type必须用数组来存储。
     * 以上两个方法返回的都是Type对象或数组，在我们的这个话题中，Class都是代表的参数化类型，
     * 因此可以将Type对象Cast成ParameterizedType对象。而 ParameterizedType对象有一个方法， getActualTypeArguments()。
     * <p>
     * public Type[] getActualTypeArguments()
     * 用来返回一个Type对象数组，这个数组代表着这个Type声明中实际使用的类型。
     */
    @SuppressWarnings("unchecked")
    private Class<T> clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    public boolean insert(@NonNull final T domain) {
        if (baseDAO.insert(domain) > 0) {
            setCache(baseDAO.queryById(domain.getId()));
            return true;
        }
        return false;
    }

    public T queryById(final int id) {
        T t = getCache(id);
        if (t != null && t.getId() != null && t.getId().equals(id)) {
            return t;
        }
        T domain = baseDAO.queryById(id);
        if (domain == null) {
            log.info("RoomDo缓存穿透：{}", id);
            setNullCache(id);
            return null;
        }
        log.info("RoomDO缓存击穿" + domain);
        setCache(domain);
        return domain;
    }

    public boolean deleteById(final int id) {
        if(baseDAO.deleteById(id) > 0) {
            tTypeRedisCache.remove(CacheKeyUtils.getIdKey(id, RoomDO.class));
            return true;
        }
        return false;
    }

    public boolean update(@NonNull final T domain) {
        if (baseDAO.update(domain) > 0) {
            setCache(baseDAO.queryById(domain.getId()));
            return true;
        }
        return false;
    }


    protected void setCache(T t) {
        tTypeRedisCache.set(CacheKeyUtils.getIdKey(t), t, CommonConst.CACHE_STORE_MINUTES);
    }

    protected void setNullCache(Integer id) {
        tTypeRedisCache.set(CacheKeyUtils.getIdKey(id, clazz), null, CommonConst.NULL_CACHE_STORE_MINUTES);
    }

    protected T getCache(Integer goId) {
        return tTypeRedisCache.get(CacheKeyUtils.getIdKey(goId, clazz));
    }

}
