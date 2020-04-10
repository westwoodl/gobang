package com.xrc.gb.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xu rongchao
 * @date 2020/3/23 20:18
 */
@Data
public class GoContext implements Serializable {
    /**
     * 棋盘大小
     */
    private Integer checkerBoardSize;
    /**
     * 位置
     */
    private List<GoPieces> placeArrays;
}
