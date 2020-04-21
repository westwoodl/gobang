package com.xrc.gb.web.controller.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author xu rongchao
 * @date 2020/4/17 16:36
 */
@Data
public class WebSocketMsgVO {

    private Integer type;

    private JSONObject content;

}
