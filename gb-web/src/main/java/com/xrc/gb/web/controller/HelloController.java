package com.xrc.gb.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.service.user.UserService;
import com.xrc.gb.web.common.JSONObjectResult;
import com.xrc.gb.web.controller.vo.HelloVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * @author xu rongchao
 * @date 2020/3/3 19:00
 */
@RequestMapping("/")
@RestController
public class HelloController extends AbstractController {

    @GetMapping
    public JSONObject hello() {
        JSONObjectResult jsonResult = JSONObjectResult.create();
        jsonResult.success("你好呀", new Date());
        jsonResult.put("1", getPageSize());
        jsonResult.put("2", getPageIndex());
        return jsonResult;
    }

    @GetMapping("/name")
    public JSONObject hello1(@RequestParam String name) {
        JSONObjectResult jsonResult = JSONObjectResult.create();
        jsonResult.success("你好呀", new Date());
        jsonResult.put("1", getPageSize());
        jsonResult.put("2", getPageIndex());
        jsonResult.put("3", name);
        return jsonResult;
    }

    @PostMapping("/hh")
    public JSONObject come(@RequestBody @Valid HelloVO helloVO) {
        return JSONObjectResult.create().success(helloVO);
    }
}
