package com.xrc.gb.common.dto.go;


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
     * {@link  com.xrc.gb.common.enums.PieceTypeEnum}
     */
    private int pieceType;

    /**
     * 是否被提子
     */
    private boolean isDead;
}
