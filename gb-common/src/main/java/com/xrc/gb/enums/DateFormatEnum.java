package com.xrc.gb.enums;

/**
 * 想要在{@link com.xrc.gb.util.DateFormatUtils}
 * 里添加，直接添加枚举常量
 *
 * @author xu rongchao
 * @date 2020/3/27 17:03
 */
public enum DateFormatEnum {

    YYYY_MM_DD("yyyy-MM-dd"),
    /**
     *
     */
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss");
    // YYYY_MM_DD_HH_MM_SS_2("yyyy/MM/dd HH:mm:ss"); // do like this direct if you want to add

    private String formatStr;

    DateFormatEnum(String formatStr) {
        this.formatStr = formatStr;
    }

    public String getFormatStr() {
        return formatStr;
    }
}
