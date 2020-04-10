package com.xrc.gb.common.enums;

import lombok.Getter;

/**
 * @author xu rongchao
 * @date 2020/4/5 9:39
 */
public enum GoStatusEnum {


    BLACK_PLACE(1, "黑方行棋"),
    WHITE_PLACE(2, "白方行棋"),
    END(0, "对局结束");

    @Getter
    private Integer code;
    @Getter
    private String desc;

    GoStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


}
