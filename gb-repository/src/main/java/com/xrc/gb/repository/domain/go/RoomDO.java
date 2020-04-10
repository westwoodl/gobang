package com.xrc.gb.repository.domain.go;

import com.xrc.gb.repository.domain.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xu rongchao
 * @date 2020/3/28 11:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoomDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = -50276990212253640L;

    /**
     * 房间当前人数
     */
    private Integer roomNumber;
    /**
     * 房间创造者
     */
    private Integer createUser;
    /**
     * 房间创造者
     */
    private String createUserName;

    private String createUserImg;

    private String createTimeStr;
    /**
     * 对手
     */
    private Integer opponents;
    private String opponentsName;
    private String opponentsImg;
    /**
     * 观战者
     */
    private String watchUser;
    /**
     * 对局id
     */
    private Integer goId;
    /**
     * 房间状态 {@link com.xrc.gb.common.enums.RoomStatusEnum}
     */
    private Integer roomStatus;

    private String roomName;

    private String roomPassword;
}