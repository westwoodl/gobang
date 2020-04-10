package com.xrc.gb.common.util;

import lombok.Data;

/**
 * @author xu rongchao
 * @date 2020/3/27 16:41
 */
@Data
public class PageQueryResultResp<T> {

    private int pageSize;

    private int pageIndex;
    /**
     * 总共有几条
     */
    private int totalCount;

    private T data;

}
