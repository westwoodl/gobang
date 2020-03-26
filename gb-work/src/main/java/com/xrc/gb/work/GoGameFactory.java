package com.xrc.gb.work;

import com.xrc.gb.enums.GameTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xu rongchao
 * @date 2020/3/26 9:02
 */
public class GoGameFactory {
    
    private static Map<GameTypeEnum, AbstractGoGameRunner> factory = new HashMap<>();
    
    public static void register(GameTypeEnum code, AbstractGoGameRunner abstractGoGameRunner) {
        factory.put(code, abstractGoGameRunner);
    }

    public static AbstractGoGameRunner getInstants(GameTypeEnum code) {
        return factory.get(code);
    }
}
