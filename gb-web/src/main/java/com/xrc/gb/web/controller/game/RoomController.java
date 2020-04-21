package com.xrc.gb.web.controller.game;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.common.enums.DateFormatEnum;
import com.xrc.gb.common.enums.GameTypeEnum;
import com.xrc.gb.common.enums.JoinRoomResultEnum;
import com.xrc.gb.common.enums.RoomStatusEnum;
import com.xrc.gb.common.dto.go.GoQueryResp;
import com.xrc.gb.repository.domain.go.RoomDO;
import com.xrc.gb.repository.domain.user.UserDO;
import com.xrc.gb.service.game.GoBangGameServiceImpl;
import com.xrc.gb.service.game.RoomServiceImpl;
import com.xrc.gb.service.user.UserService;
import com.xrc.gb.common.util.DateFormatUtils;
import com.xrc.gb.common.util.PageQueryReq;
import com.xrc.gb.common.util.PageQueryResultResp;
import com.xrc.gb.web.common.JSONObjectResult;
import com.xrc.gb.web.controller.AbstractController;
import com.xrc.gb.web.controller.vo.RoomCreateVO;
import com.xrc.gb.web.controller.vo.RoomGameVO;
import com.xrc.gb.web.controller.ws.WebSocketServer;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
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
    private GoBangGameServiceImpl goBangGameService;

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
        return JSONObjectResult.create().success(roomDO.getId());
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
        sendTwoUserByRoomId(roomId);
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
    @PutMapping("/start")
    public JSONObject startGame(@RequestParam Integer roomId, @RequestParam(required = false) Long gameTime, @RequestParam(required = false) Integer gameMode) throws IOException {
        roomService.startGame(roomId, getUserId());
        // 创建对局
        RoomDO roomDO = roomService.queryById(roomId);
        GoQueryResp goCreateReq = new GoQueryResp();
        goCreateReq.setBlackUserId(roomDO.getCreateUser());
        goCreateReq.setWhiteUserId(roomDO.getOpponents());
        if (gameTime != null) {
            goCreateReq.setEndTime(new Date(System.currentTimeMillis() + gameTime));
        }
        if (gameMode != null && GameTypeEnum.exist(gameMode)) {
            goCreateReq.setGoType(gameMode);
        }
        Integer goId = goBangGameService.createGame(goCreateReq);
        if (goId == null) {
            return JSONObjectResult.create().fail("创建对局失败");
        }
        // 创建对局成功，修改对局状态
        RoomDO updateRoomReq = new RoomDO();
        updateRoomReq.setId(roomId);
        updateRoomReq.setRoomStatus(RoomStatusEnum.GAMING.getCode());
        updateRoomReq.setGoId(goId);
        boolean isUpdateSuccess = roomService.update(updateRoomReq);
        if (isUpdateSuccess) {
            // 开始游戏后发送room和go数据
            sendTwoUserByRoomId(roomId);
            return JSONObjectResult.create().success();
        } else {
            return JSONObjectResult.create().fail("房修改失败");
        }
    }

    /**
     * 离开房间
     */
    @PutMapping("/leave")
    public JSONObject leaveRoom(@RequestParam Integer roomId) {
        boolean isSuccess = roomService.leaveRoom(roomId, getUserId());
        if (isSuccess) {
            sendTwoUserByRoomId(roomId);
            return JSONObjectResult.create().success();
        }
        return JSONObjectResult.create().fail();
    }

    private void sendTwoUserByRoomId(Integer roomId) {
        // 开始游戏后发送room和go数据
        RoomGameVO roomGameVO = new RoomGameVO();
        RoomDO roomDO = roomService.queryById(roomId);
        if (roomDO == null) {
            return;
        }
        buildRoomDO(roomDO);
        roomGameVO.setRoomDO(roomDO);
        if (roomDO.getGoId() == null) {
                WebSocketServer.send(roomGameVO, roomDO.getCreateUser());
                WebSocketServer.send(roomGameVO, roomDO.getOpponents());

        } else {
            GoQueryResp goQueryResp = goBangGameService.queryGame(roomDO.getGoId());
            roomGameVO.setGoQueryResp(goQueryResp);
                WebSocketServer.send(roomGameVO, goQueryResp.getWhiteUserId());
                WebSocketServer.send(roomGameVO, goQueryResp.getBlackUserId());
        }
    }

    /**
     * 查询room的创建者和对手的人物名称
     */
    private void buildRoomDO(RoomDO roomDO) {
        UserDO create = userService.find(roomDO.getCreateUser());
        roomDO.setCreateUserName(create.getUserName());
        roomDO.setCreateUserImg(create.getImg());
        if (roomDO.getOpponents() != null) {
            UserDO opUser = userService.find(roomDO.getOpponents());
            roomDO.setOpponentsName(opUser.getUserName());
            roomDO.setOpponentsImg(opUser.getImg());
        }
    }

}
