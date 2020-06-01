package com.xrc.gb.common.enums;

import lombok.Getter;

/**
 * @author xu rongchao
 * @date 2020/5/12 11:32
 */
public enum MessageTypeEnum {
    /**
     * 进入房间和 发送消息
     */
    JOIN_ROOM(1),
    LEAVE_ROOM(2),
    SEND_MESSAGE(3);

    @Getter
    private Integer type;

    MessageTypeEnum(Integer type) {
        this.type = type;
    }
}
