package com.xrc.gb.manager.go.dto;


import com.xrc.gb.enums.PieceTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xu rongchao
 * @date 2020/3/23 16:21
 */
@Data
public class GoPieces implements Serializable {

    private int x;

    private int y;

    private int handNum;

    /**
     * {@link  com.xrc.gb.enums.PieceTypeEnum}
     */
    private int pieceType;

    /**
     * 是否被提子
     */
    private boolean isDead;
}
