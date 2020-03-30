package com.xrc.gb.web.config;

import org.springframework.boot.web.servlet.error.ErrorController;

/**
 * @author xu rongchao
 * @date 2020/3/29 17:20
 */
public class AppErrorController  implements ErrorController {

    @Override
    public String getErrorPath() {
        return null;
    }
}
