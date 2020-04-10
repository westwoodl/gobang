package com.xrc.gb.common.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xu rongchao
 * @date 2020/4/3 9:44
 */
public enum JoinRoomResultEnum {
    YOU_ARE_WATCH(1, "您进入了观众席"),
    YOU_ARE_WHITE(2, "您是白棋"),
    YOU_ARE_BLACK(2, "您是黑棋");

    @Getter
    @Setter
    private Long expectModifyTime;

    @Getter
    @Setter
    private Integer code;
    @Getter
    @Setter
    private String desc;

    JoinRoomResultEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
