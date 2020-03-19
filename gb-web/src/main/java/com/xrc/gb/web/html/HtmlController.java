package com.xrc.gb.web.html;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author xu rongchao
 * @date 2020/3/16 10:41
 */
@Controller
public class HtmlController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }
}
