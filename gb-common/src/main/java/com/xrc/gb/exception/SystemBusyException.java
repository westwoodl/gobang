package com.xrc.gb.exception;

/**
 * @author xu rongchao
 * @date 2020/3/27 19:31
 */
public class SystemBusyException extends RuntimeException {
    public SystemBusyException(String errorMsg) {
        super(errorMsg);
    }
}
