package com.xrc.gb.manager.go.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xu rongchao
 * @date 2020/3/23 20:17
 */
@Data
public class GoQueryResp implements Serializable {

    private Integer blackUserId;

    private Integer whiteUserId;

    private GoContext goContext;

    private Integer roomId;

    private boolean isEnd;

    private Date endTime;

    private Integer lastUserId;
}