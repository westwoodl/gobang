package com.xrc.gb.work.exception;

/**
 * @author xu rongchao
 * @date 2020/3/23 16:42
 */
public class PlaceNotAllowException extends IllegalArgumentException {

    public PlaceNotAllowException(){
        super();
    }


    public PlaceNotAllowException(String str) {
        super(str);
    }
}
