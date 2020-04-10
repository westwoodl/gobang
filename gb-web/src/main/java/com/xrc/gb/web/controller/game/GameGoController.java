package com.xrc.gb.web.controller.game;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.web.common.JSONObjectResult;
import com.xrc.gb.web.controller.ws.WebSocketServer;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author xu rongchao
 * @date 2020/4/7 14:21
 */
@RestController
@RequestMapping("/go")
public class GameGoController {
    @GetMapping("/query")
    public JSONObject index() {
        return JSONObjectResult.create().success();
    }

    @PostMapping("/create")
    public JSONObject page() {
        return JSONObjectResult.create().success();
    }

    @PutMapping("/update")
    public JSONObject update() {
        return JSONObjectResult.create().success();
    }

    @RequestMapping("/push/{toUserId}")
    public JSONObject pushToWeb(String message, @PathVariable String toUserId) throws IOException {
        WebSocketServer.sendInfo(message, toUserId);
        return JSONObjectResult.create().success("MSG SEND SUCCESS");
    }
}
