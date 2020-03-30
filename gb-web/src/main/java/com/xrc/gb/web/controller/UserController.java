package com.xrc.gb.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.service.user.UserService;
import com.xrc.gb.web.common.JSONObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xu rongchao
 * @date 2020/3/4 21:29
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public JSONObject getUser(@PathVariable Integer id) {
        return JSONObjectResult.create().success(userService.find(id));
    }


    @GetMapping("/login")
    public JSONObject login(@RequestParam(required = false) String account,
                            @RequestParam(required = false) String password) {
        return JSONObjectResult.create().success();
    }

    @PostMapping
    public JSONObject registry(
            @RequestParam String userName,
            @RequestParam String account,
            @RequestParam String password) {

        userService.add(userName, account, password);
        return JSONObjectResult.create().success();
    }


}
