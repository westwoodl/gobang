package com.xrc.gb.common.util;

/**
 * @author xu rongchao
 * @date 2020/4/8 16:27
 */
public class Defaults {
    public static final Integer ZERO = 0;
    public static final Character DEFAULT_CHAR = Character.MIN_VALUE;
    public static final Boolean FALSE = false;
    public static final Object NULL = null;

    private Defaults() {
    }

    public static <T> T defaultValue(Class<T> clazz) {
        if (clazz.equals(Boolean.TYPE)) {
            return (T) Boolean.FALSE;
        }
        if (clazz.equals(Integer.TYPE) || clazz.equals(Float.TYPE)|| clazz.equals(Double.TYPE)) {
            return (T) ZERO;
        }
        if (clazz.equals(Character.TYPE)) {
            return (T) DEFAULT_CHAR;
        }
        return null;
    }
}
