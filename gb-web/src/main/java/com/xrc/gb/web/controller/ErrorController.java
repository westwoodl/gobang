package com.xrc.gb.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.web.common.JSONObjectResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xu rongchao
 * @date 2020/3/9 20:52
 */
@RestController
@RequestMapping("/xrc")
public class ErrorController {

    @RequestMapping("/error")
    public JSONObject error(@RequestParam String msg, BindingResult bindingResult) {

        JSONObjectResult jsonObjectResult = JSONObjectResult.create().fail(msg);

        if(bindingResult.hasErrors()) {
            jsonObjectResult.put("msg", bindingResult.getAllErrors());
        }
        return jsonObjectResult;
    }
}
