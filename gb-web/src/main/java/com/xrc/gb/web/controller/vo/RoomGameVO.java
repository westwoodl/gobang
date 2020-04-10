package com.xrc.gb.web.controller.vo;

import com.xrc.gb.common.dto.GoQueryResp;
import com.xrc.gb.repository.domain.go.RoomDO;
import lombok.Data;

/**
 * @author xu rongchao
 * @date 2020/4/9 18:00
 */
@Data
public class RoomGameVO {
    private RoomDO roomDO;

    private GoQueryResp goQueryResp;
}
