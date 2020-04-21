package com.xrc.gb.common.enums;

import lombok.Getter;

/**
 * @author xu rongchao
 * @date 2020/4/17 16:30
 */
public enum  WebSocketMessageTypeEnum {
    /**
     *
     */
    ROOM_CHANGE_MSG(1),

    GO_CHANGE_MEG(2),

    CHAT_CHANGE_MSG(3);

    @Getter
    private final Integer code;

    WebSocketMessageTypeEnum(int i) {
        code = i;
    }

}
