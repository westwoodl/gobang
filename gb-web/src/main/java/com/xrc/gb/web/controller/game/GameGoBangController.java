package com.xrc.gb.web.controller.game;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.common.enums.PlaceResultTypeEnum;
import com.xrc.gb.common.dto.GoContext;
import com.xrc.gb.common.dto.GoPieces;
import com.xrc.gb.common.dto.GoPlaceReq;
import com.xrc.gb.common.dto.GoQueryResp;
import com.xrc.gb.service.game.GoBangGameServiceImpl;
import com.xrc.gb.common.util.PageQueryReq;
import com.xrc.gb.service.game.RoomServiceImpl;
import com.xrc.gb.web.common.JSONObjectResult;
import com.xrc.gb.web.controller.AbstractController;
import com.xrc.gb.web.controller.vo.GoBangGameVO;
import com.xrc.gb.web.controller.vo.RoomGameVO;
import com.xrc.gb.web.controller.ws.WebSocketServer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 对局创建、查询、落子
 *
 * @author xu rongchao
 * @date 2020/3/26 9:30
 */
@RestController
@RequestMapping("/gobang")
public class GameGoBangController extends AbstractController {

    @Resource
    private GoBangGameServiceImpl goBangGameService;


    @Resource
    private RoomServiceImpl roomService;


    /**
     * 单一查询
     */
    @GetMapping("/query")
    public JSONObject query(@RequestParam Integer goId) {
        return JSONObjectResult.create().success(goBangGameService.queryGame(goId));
    }

    @GetMapping("/queryBlock")
    public JSONObject queryBlock(@RequestParam Integer goId, @RequestParam(value = "expectTime") Long expectModifyTime) {
        return JSONObjectResult.create().success(goBangGameService.queryGameBlock(goId, expectModifyTime));
    }

    @PutMapping("/place")
    public JSONObject place(@RequestParam Integer id, @RequestParam Integer x, @RequestParam Integer y) throws IOException {
        GoPlaceReq goPlaceReq = new GoPlaceReq();
        goPlaceReq.setId(id);
        goPlaceReq.setUserId(getUserId());
        GoPieces goPieces = new GoPieces();
        goPieces.setX(x);
        goPieces.setY(y);
        goPlaceReq.setGoPieces(goPieces);
        PlaceResultTypeEnum placeResultTypeEnum = goBangGameService.updateGameForPlace(goPlaceReq);
        // 下棋不透传room数据
        RoomGameVO roomGameVO = new RoomGameVO();
        GoQueryResp goQueryResp = goBangGameService.queryGame(id);
        roomGameVO.setRoomDO(null);
        roomGameVO.setGoQueryResp(goQueryResp);
        if (goQueryResp.getBlackUserId().equals(getUserId())) {
            WebSocketServer.send(roomGameVO, goQueryResp.getWhiteUserId());
        }
        if (goQueryResp.getWhiteUserId().equals(getUserId())) {
            WebSocketServer.send(roomGameVO, goQueryResp.getBlackUserId());
        }

        return JSONObjectResult.create().success(placeResultTypeEnum.getDesc(), placeResultTypeEnum.getPlaceTime());
    }

    @GetMapping("/queryByUserId")
    public JSONObject queryByUserId(@RequestParam(required = false) String name) {
        PageQueryReq<GoQueryResp> pageQueryReq = getPageQueryReq();
        GoQueryResp goQueryResp = new GoQueryResp();
        //goQueryResp.set; //todo 添加房间名
        goQueryResp.setBlackUserId(getUserId());
        goQueryResp.setWhiteUserId(getUserId());
        pageQueryReq.setData(goQueryResp);

        return JSONObjectResult.create().success(goBangGameService.queryGameList(pageQueryReq));
    }


    private GoQueryResp buildGoQueryResp(GoBangGameVO goBangGameVO) {
        GoQueryResp goQueryResp = new GoQueryResp();
        goQueryResp.setBlackUserId(goBangGameVO.getBlackUserId());
        goQueryResp.setWhiteUserId(goBangGameVO.getWhiteUserId());
        GoContext goContext = new GoContext();
        goContext.setCheckerBoardSize(goBangGameVO.getBoardSize());
        goQueryResp.setEndTime(goBangGameVO.getEndTime());
        return goQueryResp;
    }

}
