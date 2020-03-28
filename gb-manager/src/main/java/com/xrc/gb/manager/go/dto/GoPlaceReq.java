package com.xrc.gb.manager.go.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xu rongchao
 * @date 2020/3/28 8:54
 */
@Data
public class GoPlaceReq implements Serializable {

    private Integer id;

    private Integer userId;

    private GoPieces goPieces;
}
