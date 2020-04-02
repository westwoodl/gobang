package com.xrc.gb.enums;

/**
 * @author xu rongchao
 * @date 2020/4/2 13:30
 */
public enum  UserStatusEnum {

    IDLE(1, "在线"),
    GAMING(2, "游戏中"),
    ENDED(0, "离线");

    private Integer code;
    private String desc;

    UserStatusEnum(int code, String desc) {
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
        for (UserStatusEnum s : UserStatusEnum.values()) {
            if(s.getCode().equals(co)) {
                return s.getDesc();
            }
        }
        return null;
    }
}
