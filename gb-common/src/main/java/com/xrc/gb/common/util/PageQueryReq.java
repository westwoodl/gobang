package com.xrc.gb.common.util;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xu rongchao
 * @date 2020/3/27 16:41
 */
@Data
public class PageQueryReq<T> {

    /**
     * todo 试用
     * @param count
     * @param <U>
     * @return
     */
    public <U> PageQueryResultResp<List<U>> getQueryResult(int count) {
        PageQueryResultResp<List<U>> pageQueryResultResp = new PageQueryResultResp<>();
        pageQueryResultResp.setPageIndex(pageIndex);
        pageQueryResultResp.setPageSize(pageSize);
        pageQueryResultResp.setTotalCount(count);
        pageQueryResultResp.setData(new ArrayList<>());
        return pageQueryResultResp;
    }

    private int pageSize;

    private int pageIndex;

    private T data;

    public int getOffSet() {
        return (1-pageIndex) * pageSize;
    }

}
