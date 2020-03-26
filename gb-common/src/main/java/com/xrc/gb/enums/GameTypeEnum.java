package com.xrc.gb.enums;

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

    WEI_QI(1),
    GO_BANG(2);

    private Integer code;

    GameTypeEnum(Integer code) {
        this.code = code;
    }
}
