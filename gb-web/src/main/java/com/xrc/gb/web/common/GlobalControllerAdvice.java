package com.xrc.gb.web.common;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * 全局异常解析
 *
 * @author xu rongchao
 * @date 2020/3/9 20:32
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * 处理NullPointerException，并返回错误信息字符串
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public String handleIOException(NullPointerException ex) {
        return ex.getMessage();
    }

    /**
     * 处理IOException，并返回502错误, 即"Bad Gateway"
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public void handleIOException(IOException ex) {

    }


    /**
     * 处理所有Exception类型异常
     * 但请注意，如果该ControllerAdvice内已经有其他方法处理了指定异常，那就不会进入到该方法处理
     * 比如发生Custom3Exception异常，那么只会在@ExceptionHandler(Custom3Exception.class)的方法中处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView Exception(Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("msg", e.getMessage());
        modelAndView.setViewName("/xrc/error");
        return modelAndView;
    }

}
