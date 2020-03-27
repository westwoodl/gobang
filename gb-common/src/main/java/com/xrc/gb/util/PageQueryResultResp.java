package com.xrc.gb.util;

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
     * 总共有几页
     */
    private int totalPage;

    private T data;

}
