package com.xrc.gb.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xu rongchao
 * @date 2020/3/23 20:17
 */
@Data
public class GoQueryResp implements Serializable {
    private Integer id;

    private Integer blackUserId;

    private Integer whiteUserId;

    private GoContext goContext;

    @Deprecated
    private Integer roomId;

    private Integer isEnd;

    private Date modifyTime;

    private Date endTime;

    private Integer lastUserId;

    private Integer goStatus;

    /**
     * {@link com.xrc.gb.common.enums.GameTypeEnum}
     */
    private Integer goType;
}
