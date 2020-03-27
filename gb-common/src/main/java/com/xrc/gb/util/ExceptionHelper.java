package com.xrc.gb.util;

import com.xrc.gb.exception.SystemBusyException;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xu rongchao
 * @date 2020/3/27 19:20
 */
public class ExceptionHelper {

    @Data
    @AllArgsConstructor
    @Deprecated
    public static class ExceptionInfo {
        private String errorCode;
        private String errorMsg;
    }

    public static SystemBusyException newBizException(String exceptionInfo, Throwable e) {
        return new SystemBusyException(exceptionInfo + ":" + e.getMessage());
    }

    public static RuntimeException newBizException(String exceptionInfo) {
        return new SystemBusyException(exceptionInfo);
    }

}
