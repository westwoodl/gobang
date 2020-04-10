package com.xrc.gb.web.config;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.common.exception.BusinessException;
import com.xrc.gb.common.exception.SystemBusyException;
import com.xrc.gb.web.common.JSONObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

/**
 * 全局异常解析
 *
 * @author xu rongchao
 * @date 2020/3/9 20:32
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionControllerAdvice {

    @ExceptionHandler(SystemBusyException.class)
    @ResponseBody
    public JSONObject handleSystemBusyException(SystemBusyException ex) {
        return JSONObjectResult.create().fail(ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public JSONObject handleBusinessException(BusinessException ex) {
        return JSONObjectResult.create().fail(ex.getMessage());
    }

    /**
     * 处理IOException，并返回502错误, 即"Bad Gateway"
     */
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public void handleIOException(IOException ex) {
    }


}
