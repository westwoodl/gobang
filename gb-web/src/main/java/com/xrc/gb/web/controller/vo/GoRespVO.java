package com.xrc.gb.web.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xu rongchao
 * @date 2020/4/11 13:44
 */
@Data
public class GoRespVO implements Serializable {

    /**
     * 序号
     */
    private Integer id;
    /**
     * 对局名
     */
    private String gameName;
    /**
     * 黑棋
     */
    private String blackUserName;
    /**
     * 白棋
     */
    private String whiteUserName;
    /**
     * 对局状态
     */
    private Integer goStatus;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 结果
     */
    private Integer goResult;
    /**
     * 游戏模式
     */
    private Integer goType;
}
