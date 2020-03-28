package com.xrc.gb.enums;

/**
 * @author xu rongchao
 * @date 2020/3/28 19:17
 */
public enum  RoomStatusEnum {

    IDLE(1, "空闲中"),
    GAMING(2, "对局中"),
    ENDED(3, "已结束");

    private Integer code;
    private String desc;

    RoomStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }


    public String getDesc() {
        return desc;
    }


    public String getDescByCode(Integer co) {
        for (RoomStatusEnum s : RoomStatusEnum.values()) {
            if(s.getCode().equals(co)) {
                return s.getDesc();
            }
        }
        return null;
    }
}
