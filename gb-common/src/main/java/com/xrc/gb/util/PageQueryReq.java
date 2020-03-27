package com.xrc.gb.util;

import lombok.Data;

/**
 * @author xu rongchao
 * @date 2020/3/27 16:41
 */
@Data
public class PageQueryReq<T> {

    private int pageSize;

    private int pageIndex;

    private T data;
}
