package com.xrc.gb.exception;

/**
 * @author xu rongchao
 * @date 2020/3/28 9:08
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String msg) {
        super(msg);
    }
}
