package com.xrc.gb.web.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author xu rongchao
 * @date 2020/3/28 19:42
 */
@Data
public class RoomCreateVO implements Serializable {
    @NotNull(message = "房间名不能为空")
    private String roomName;

    private String roomPassword;
}
