package com.xrc.gb.repository.domain.user;

import com.xrc.gb.repository.domain.BaseDO;
import lombok.Data;

/**
 * @author xu rongchao
 * @date 2020/3/4 10:31
 */
@Data
public class UserDO extends BaseDO {

    private String userName;

    private String account;

    private String password;

    private Integer userPower;

    private String img;
    private String autograph;
    private String friend;

    private Integer status;

}
