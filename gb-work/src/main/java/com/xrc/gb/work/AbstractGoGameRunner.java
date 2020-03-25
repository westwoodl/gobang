package com.xrc.gb.work;

import com.xrc.gb.enums.PlaceResultTypeEnum;
import com.xrc.gb.manager.go.dto.GoContext;
import com.xrc.gb.manager.go.dto.GoQueryResp;

/**
 * 下棋基类
 * @author xu rongchao
 * @date 2020/3/23 16:15
 */
public abstract class AbstractGoGameRunner {

    /**
     * 落子
     */
    public abstract PlaceResultTypeEnum place(GoContext goContext);
    /**
     * 构建 二维数组
     * @param goContext
     * @return
     */
    public abstract int[][] buildArrays(GoContext goContext);
    /**
     * 判断最后一步棋是否结束对局
     *
     */
    public abstract boolean judgeLast(int[][] goArrays, int lastX, int lastY);
    /**
     * 从头到尾判断 对局是否结束
     */
    public abstract boolean judgeAll(int[][] goArrays);
}
