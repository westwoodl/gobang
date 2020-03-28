package com.xrc.gb.web.html;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

/**
 * @author xu rongchao
 * @date 2020/3/16 10:41
 * @see com.xrc.gb.web.config.MyWebMvcConfiguration#addViewControllers(ViewControllerRegistry)
 */
@Deprecated
public class HtmlController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }
}
