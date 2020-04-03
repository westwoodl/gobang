package com.xrc.gb.web.controller.game;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.enums.DateFormatEnum;
import com.xrc.gb.enums.JoinRoomResultEnum;
import com.xrc.gb.enums.RoomStatusEnum;
import com.xrc.gb.repository.domain.go.RoomDO;
import com.xrc.gb.repository.domain.user.UserDO;
import com.xrc.gb.service.game.RoomServiceImpl;
import com.xrc.gb.service.user.UserService;
import com.xrc.gb.util.DateFormatUtils;
import com.xrc.gb.util.PageQueryReq;
import com.xrc.gb.util.PageQueryResultResp;
import com.xrc.gb.web.common.JSONObjectResult;
import com.xrc.gb.web.controller.AbstractController;
import com.xrc.gb.web.controller.vo.RoomCreateVO;
import com.xrc.gb.web.controller.vo.RoomQueryRespVO;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xu rongchao
 * @date 2020/3/28 19:04
 */
@RestController
@RequestMapping("/room")
public class RoomController extends AbstractController {

    @Resource
    private RoomServiceImpl roomService;

    @Resource
    private UserService userService;

    /**
     * 创建房间
     */
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
        PageQueryReq<RoomDO> roomDOPageQueryReq = getPageQueryReq();
        roomDOPageQueryReq.setData(new RoomDO());
        PageQueryResultResp<List<RoomDO>> paRoomDOPage = roomService.queryPage(roomDOPageQueryReq);
        for (int i = 0; i < paRoomDOPage.getData().size(); i++) {
            paRoomDOPage.getData().get(i).setCreateUserName(userService.find(paRoomDOPage.getData().get(i).getCreateUser()).getUserName());
            paRoomDOPage.getData().get(i).setCreateTimeStr(DateFormatUtils.parseDate(paRoomDOPage.getData().get(i).getCreateTime(),
                    DateFormatEnum.YYYY_MM_DD_HH_MM_SS));

        }
        return JSONObjectResult.create().success(paRoomDOPage);
    }

    @GetMapping("/{id}")
    public JSONObject queryBuId(@PathVariable Integer id) {
        RoomDO roomDO = roomService.queryById(id);
        buildRoomDO(roomDO);
        return JSONObjectResult.create().success(roomDO);
    }

    /**
     * 加入房间
     */
    @PutMapping("/join")
    public JSONObject joinRoom(@RequestParam Integer roomId) {
        UserDO joinUserDO = getUserDO();
        JoinRoomResultEnum joinRoomResultEnum = roomService.joinRoom(roomId, joinUserDO);
        return JSONObjectResult.create().success(joinRoomResultEnum.getDesc(), joinRoomResultEnum.getExpectModifyTime());
    }

    /**
     * 阻塞查询
     */
    @GetMapping("/queryBlock")
    public JSONObject queryBlock(@RequestParam Integer roomId, @RequestParam Long expectTime) {
        RoomDO roomDO = roomService.queryBlock(roomId, expectTime);
        buildRoomDO(roomDO);
        return JSONObjectResult.create().success(roomDO);
    }

    /**
     * 开始游戏
     */
    @GetMapping("start")
    public JSONObject startGame(@RequestParam Integer roomId) {
        roomService.startGame(roomId, getUserId());
        return JSONObjectResult.create().success();
    }

    /**
     * 查询room的创建者和对手的人物名称
     */
    private RoomDO buildRoomDO(RoomDO roomDO) {
        UserDO create = userService.find(roomDO.getCreateUser());
        roomDO.setCreateUserName(create.getUserName());
        roomDO.setCreateUserImg(create.getImg());
        if (roomDO.getOpponents() != null) {
            UserDO opUser = userService.find(roomDO.getOpponents());
            roomDO.setOpponentsName(opUser.getUserName());
            roomDO.setOpponentsImg(opUser.getImg());
        }
        return roomDO;
    }

}
