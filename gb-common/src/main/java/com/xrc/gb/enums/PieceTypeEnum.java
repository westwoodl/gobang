package com.xrc.gb.enums;

import lombok.Data;

/**
 * @author xu rongchao
 * @date 2020/3/23 20:33
 */
public enum PieceTypeEnum {
    BLACK_PIECE(1, "黑棋"),
    WHITE_PIECE(2, "白棋");


    private Integer code;
    private String desc;

    PieceTypeEnum(Integer type, String desc) {
        this.code = type;
        this.desc = desc;
    }

    public String getDescByCode(Integer co) {
        for (PieceTypeEnum s : PieceTypeEnum.values()) {
            if(s.getCode().equals(co)) {
                return s.getDesc();
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
