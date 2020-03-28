package com.xrc.gb.enums;

/**
 * @author xu rongchao
 * @date 2020/3/25 19:13
 */
public enum PlaceResultTypeEnum {

    WHITE_WIN_GAME(1, "白胜"),
    BLACK_WIN_GAME(2, "黑胜"),
    PLACING_PIECES_SUCCESS(3, "落子成功");

    private int code;

    private String desc;

    PlaceResultTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int getCode() {
        return code;
    }
}
