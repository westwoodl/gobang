package com.xrc.gb.repository.domain.go;

import com.xrc.gb.repository.domain.BaseDO;
import lombok.Data;

import java.util.Date;

/**
 * @author xu rongchao
 * @date 2020/3/23 17:03
 */
@Data
public class GoDO extends BaseDO {

    private Integer blackUserId;

    private Integer whiteUserId;

    private String goContext;

    @Deprecated
    private Integer roomId;

    private Integer isEnd;

    private Date endTime;

    private Integer lastUserId;

    private Integer goResult;

    private Integer goStatus;

    private Integer goType;
}
