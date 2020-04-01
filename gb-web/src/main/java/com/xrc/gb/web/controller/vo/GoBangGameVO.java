package com.xrc.gb.web.controller.vo;

import com.xrc.gb.manager.go.dto.GoContext;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author xu rongchao
 * @date 2020/3/28 9:45
 */
@Data
@ToString
public class GoBangGameVO implements Serializable {
    @NotNull(message = "用户名不能为空")
    private Integer blackUserId;
    @NotNull(message = "用户名不能为空")
    private Integer whiteUserId;
    @NotNull(message = "棋盘大小不能为空")
    private Integer boardSize;
//    @NotNull(message = "房间号不能为空")
//    private Integer roomId;


    private Date endTime;

}
