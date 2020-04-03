package com.xrc.gb.manager;

import com.xrc.gb.consts.CommonConst;
import com.xrc.gb.repository.cache.CacheKeyUtils;
import com.xrc.gb.repository.cache.TypeRedisCache;
import com.xrc.gb.repository.dao.BaseDAO;
import com.xrc.gb.repository.domain.BaseDO;
import com.xrc.gb.repository.domain.go.GoDO;
import com.xrc.gb.repository.domain.user.UserDO;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;

/**
 * todo
 *
 * @author xu rongchao
 * @date 2020/4/3 20:59
 */
public abstract class AbstractCacheManager<T extends BaseDO>{

    //在Spring Boot2.0中可以直接在父类属性上加入注解
    @Autowired
    protected TypeRedisCache<T> tTypeRedisCache;
    /**
     * 1. public Type getGenericSuperclass()
     * 用来返回表示当前Class 所表示的实体（类、接口、基本类型或 void）的直接超类的Type。如果这个直接超类是参数化类型的，
     * 则返回的Type对象必须明确反映在源代码中声明时使用的类型
     *
     * 2. public Type[] getGenericInterfaces()
     *     与上面那个方法类似，只不过Java的类可以实现多个接口，所以返回的Type必须用数组来存储。
     *     以上两个方法返回的都是Type对象或数组，在我们的这个话题中，Class都是代表的参数化类型，
     *     因此可以将Type对象Cast成ParameterizedType对象。而 ParameterizedType对象有一个方法， getActualTypeArguments()。
     *
     * public Type[] getActualTypeArguments()
     * 用来返回一个Type对象数组，这个数组代表着这个Type声明中实际使用的类型。
     */
    @SuppressWarnings("unchecked")
    private Class<T> clazz = (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    public abstract int insert(UserDO domain) ;

    public abstract UserDO queryById(int id) ;

    public int deleteById(int id) {
        throw new UnsupportedOperationException();
    }
    public abstract int update(UserDO domain);



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
