package com.xrc.gb.web.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author xu rongchao
 * @date 2020/3/9 18:22
 */
@Data
public class HelloVO {
    //@NotNull(message = "name不能为空")
    private String name;
    @NotNull(message = "user不能为空")
    private String user;
}
