package com.xrc.gb.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.repository.domain.user.UserDO;
import com.xrc.gb.service.user.UserService;
import com.xrc.gb.web.common.JSONObjectResult;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/load")
    public JSONObject load() {
        UserDO userDO = getUserDO();
        return JSONObjectResult.create().success("欢迎", userDO);
    }

    @GetMapping("/login")
    public JSONObject login(@RequestParam String account,
                            @RequestParam String password) {
        UserDO userDO = userService.login(account, password);
        if (userDO != null) {
            addUserSession(userDO);
            return JSONObjectResult.create().success("登录成功", userDO);
        }
        return JSONObjectResult.create().fail("用户名或密码错误");
    }

    @GetMapping("/out")
    public JSONObject loginOut() {
        deleteUserSession();
        return JSONObjectResult.create().success("登出成功");
    }

    @GetMapping("/friend")
    public JSONObject friend() {
        UserDO userDO = userService.find(getUserId());
        List<Integer> friendList = JSONObject.parseArray(userDO.getFriend(), Integer.class);
        List<UserDO> friendDOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(friendList)) {
            for (Integer friendId : friendList) {
                UserDO friendDO = userService.find(friendId);
                if (friendDO!=null) {
                    friendDOList.add(friendDO);
                }
            }
        }
        return JSONObjectResult.create().success(friendDOList);
    }

    /**
     * 添加朋友
     */
    @PostMapping("/friend")
    public JSONObject addFriend(@RequestParam Integer id) {
        return JSONObjectResult.create().success(userService.addFriend(id, getUserId()));
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
