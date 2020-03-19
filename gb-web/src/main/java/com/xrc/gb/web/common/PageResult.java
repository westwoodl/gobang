package com.xrc.gb.web.common;

import lombok.Data;

/**
 * @author xu rongchao
 * @date 2020/3/3 20:02
 */
@Data
public class PageResult<T> {
    private String msg;

    private String code;


    public void setDataList(T t) {

    }
}
