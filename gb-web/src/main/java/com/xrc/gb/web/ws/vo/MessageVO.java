package com.xrc.gb.web.ws.vo;

import com.xrc.gb.repository.domain.user.UserDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xu rongchao
 * @date 2020/5/11 14:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageVO {
    private Integer num;

    private Long time;

    private UserDO userDO;

    private String message;

    private Integer messageType;
}
