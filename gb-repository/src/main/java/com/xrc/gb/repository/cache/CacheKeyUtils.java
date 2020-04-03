package com.xrc.gb.repository.cache;

import com.xrc.gb.repository.domain.BaseDO;
import com.xrc.gb.repository.domain.go.RoomDO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * 获取redis唯一标识
 *
 * @author xu rongchao
 * @date 2020/4/3 15:56
 */
public class CacheKeyUtils {

    public static <T> String getPageKey(T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        StringBuilder key = new StringBuilder();
        key.append(getShortClassName(t));
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if (field.get(t) == null || "serialVersionUID".equals(field.getName())) {
                    continue;
                }
                key.append(field.getName()).append("_").append(field.get(t)).append("_");
            } catch (IllegalAccessException ignored) {
            }
        }
        return key.toString().toUpperCase();
    }

    public static <T extends BaseDO> String getIdKey(T t) {
        return (getShortClassName(t) + "_" + t.getId()).toUpperCase();
    }

    public static <T extends BaseDO> String getIdKey(int id, Class<T> clazz) {
        return (getShortClassName(clazz) + "_" + id).toUpperCase();
    }

    protected static <T> String getShortClassName(Class<T> clazz) {
        String[] strArr = clazz.getName().split("\\.");
        if (strArr.length > 0) {
            return strArr[strArr.length - 1];
        }
        return clazz.getName();
    }

    protected static <T> String getShortClassName(T t) {
        return getShortClassName(t.getClass());
    }

}
