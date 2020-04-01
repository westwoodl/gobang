package com.xrc.gb.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.repository.domain.user.UserDO;
import com.xrc.gb.service.user.UserService;
import com.xrc.gb.web.common.JSONObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author xu rongchao
 * @date 2020/3/4 21:29
 */
@RestController
@RequestMapping("/user")
public class UserController extends AbstractController {
    @Autowired
    UserService userService;

    @GetMapping("/{id}")
    public JSONObject getUser(@PathVariable Integer id) {
        return JSONObjectResult.create().success(userService.find(id));
    }


    @GetMapping("/login")
    public JSONObject login(@RequestParam String account,
                            @RequestParam String password, HttpServletRequest httpServletRequest) {
        UserDO userDO = userService.login(account, password);
        HttpSession httpSession = httpServletRequest.getSession();
        boolean ise = httpServletRequest == RequestContextHolder.getRequestAttributes();
        if (userDO != null) {
            addUserSession(userDO);
            return JSONObjectResult.create().success("登录成功", userDO);
        }
        return JSONObjectResult.create().fail("用户名或密码错误");
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
