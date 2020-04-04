package com.xrc.gb.util;

/**
 * @author xu rongchao
 * @date 2020/3/25 22:16
 */
public class CheckParameter {
    private CheckParameter() {
    }
    
    public static void isNotNull(Object o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
    }

    public static void isNotNull(Object o, String errorMsg) {
        if (o == null) {
            throw new IllegalArgumentException(errorMsg);
        }
    }

    public static void assertTrue(boolean s) {
        if (!s) {
            throw new IllegalArgumentException();
        }
    }
    
    
}
