package com.xrc.gb.web.controller.vo;

import lombok.Data;

/**
 * @author xu rongchao
 * @date 2020/4/2 14:53
 */
@Data
public class RoomQueryRespVO {


    /**
     * 房间当前人数
     */
    private Integer roomNumber;

    private String createUserId;

    /**
     * 房间创造者
     */
    private String createUser;
    /**
     * 对手
     */
    private Integer opponents;
    /**
     * 观战者
     */
    private String watchUser;
    /**
     * 对局id
     */
    private Integer goId;
    /**
     * 房间状态
     */
    private Integer roomStatus;

    private String roomName;

    private String roomPassword;
}
