package com.xrc.gb.repository.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xu rongchao
 * @date 2020/3/4 10:33
 */
@Data
public class BaseDO implements Serializable {
    private Integer id;

    private Date createTime;

    private Date modifyTime;
}
