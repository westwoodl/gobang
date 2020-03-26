package com.xrc.gb.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.web.common.JSONObjectResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author xu rongchao
 * @date 2020/3/26 9:30
 */
@RestController
@RequestMapping("/gobang")
public class GameGoBangController {

    @PutMapping
    public JSONObject place() {


        return JSONObjectResult.create().success();
    }


    @PostMapping
    public JSONObject create() {


        return JSONObjectResult.create().success("创建成功");
    }

    @GetMapping
    public JSONObject query(@RequestParam Long goId) {



        return JSONObjectResult.create().success();
    }
}
