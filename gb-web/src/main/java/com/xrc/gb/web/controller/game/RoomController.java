package com.xrc.gb.web.controller.game;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.enums.RoomStatusEnum;
import com.xrc.gb.repository.domain.go.RoomDO;
import com.xrc.gb.service.game.RoomServiceImpl;
import com.xrc.gb.util.PageQueryReq;
import com.xrc.gb.web.common.JSONObjectResult;
import com.xrc.gb.web.controller.AbstractController;
import com.xrc.gb.web.controller.vo.RoomCreateVO;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author xu rongchao
 * @date 2020/3/28 19:04
 */
@RestController
@RequestMapping("/room")
public class RoomController extends AbstractController {

    @Resource
    private RoomServiceImpl roomService;

    @PostMapping("/create")
    public JSONObject createRoom(@RequestBody @Validated RoomCreateVO roomVO, BindingResult bindingResult) {
        hasError(bindingResult);
        RoomDO roomDO = new RoomDO();
        roomDO.setCreateUser(getUserId());
        roomDO.setRoomStatus(RoomStatusEnum.IDLE.getCode());
        roomDO.setRoomName(roomVO.getRoomName());
        roomDO.setRoomPassword(roomVO.getRoomPassword());
        roomService.create(roomDO);
        return JSONObjectResult.create().success();
    }

    @GetMapping
    public JSONObject queryPage() {
        roomService.queryPage(getPageQueryReq());
        return JSONObjectResult.create().success();
    }

    @PutMapping
    public JSONObject update() {
        roomService.update(new RoomDO());
        return JSONObjectResult.create().success();
    }

}
