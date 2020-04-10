package com.xrc.gb.common.enums;

import lombok.Getter;

/**
 * （1）、自定义的Enum类被编译器优化为final类，类似于String的常量类
 * （2）、自定义的Enum类继承自 java.lang.Enum 类
 * （3）、语法糖，自定义枚举通过反编译可以看到该类添加了 values（）、valueOf（）方法。
 * （4）、equals、hashCode 已经被重写。
 *
 * @author xu rongchao
 * @date 2020/3/26 9:19
 */
public enum GameTypeEnum {

    /**
     * 游戏类型
     */
    WEI_QI(1),
    GO_BANG(2);

    @Getter
    private Integer code;

    GameTypeEnum(Integer code) {
        this.code = code;
    }

    /**
     * 我纠结于该方法是不是要写一个判空，但我又实在想不到有什么操蛋的情况会传入一个null
     */
    public static GameTypeEnum getEnum(Integer code) {
        for (GameTypeEnum g : GameTypeEnum.values()){
            if(g.getCode().equals(code)) {
                return g;
            }
        }
        return null;
    }

    public static boolean exist(Integer code) {
        for (GameTypeEnum g : GameTypeEnum.values()){
            if(g.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
}
