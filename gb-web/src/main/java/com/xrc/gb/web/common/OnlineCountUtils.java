package com.xrc.gb.web.common;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xu rongchao
 * @date 2020/4/11 9:56
 */
public class OnlineCountUtils {
    private OnlineCountUtils() {}

    private static AtomicInteger onlineCount = new AtomicInteger(0);



    public static int getOnlineCount() {
        return onlineCount.get();
    }

    public static void addOnlineCount() {
        onlineCount.getAndIncrement();
    }

    public static void subOnlineCount() {
        onlineCount.getAndDecrement();
    }
}
