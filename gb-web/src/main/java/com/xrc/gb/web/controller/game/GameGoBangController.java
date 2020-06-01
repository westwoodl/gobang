package com.xrc.gb.web.controller.game;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.common.enums.DateFormatEnum;
import com.xrc.gb.common.enums.PlaceResultTypeEnum;
import com.xrc.gb.common.dto.go.GoContext;
import com.xrc.gb.common.dto.go.GoPieces;
import com.xrc.gb.common.dto.go.GoPlaceReq;
import com.xrc.gb.common.dto.go.GoQueryResp;
import com.xrc.gb.common.util.DateFormatUtils;
import com.xrc.gb.common.util.PageQueryResultResp;
import com.xrc.gb.service.game.GoBangGameServiceImpl;
import com.xrc.gb.common.util.PageQueryReq;
import com.xrc.gb.service.user.UserService;
import com.xrc.gb.web.common.JSONObjectResult;
import com.xrc.gb.web.controller.AbstractController;
import com.xrc.gb.web.controller.vo.GoBangGameVO;
import com.xrc.gb.web.controller.vo.GoRespVO;
import com.xrc.gb.web.controller.vo.RoomGameVO;
import com.xrc.gb.web.ws.WebSocketServer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

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
    private UserService userService;


    @PutMapping("/defeat")
    public JSONObject gobangDefeat(@RequestParam("id") Integer goId) {
        goBangGameService.defeat(goId, getUserId());
        GoQueryResp goQueryResp = goBangGameService.queryGame(goId);
        RoomGameVO roomGameVO = new RoomGameVO();
        roomGameVO.setGoQueryResp(goQueryResp);
        WebSocketServer.send(roomGameVO, goQueryResp.getBlackUserId(), goQueryResp.getWhiteUserId());
        return JSONObjectResult.create().success();
    }

    @GetMapping
    public JSONObject queryPage() {
        PageQueryReq<GoQueryResp> pageQueryReq = getPageQueryReq();
        GoQueryResp goQueryResp = new GoQueryResp();
        pageQueryReq.setData(goQueryResp);
        goQueryResp.setBlackUserId(getUserId());
        goQueryResp.setWhiteUserId(getUserId());
        PageQueryResultResp<List<GoQueryResp>> page = goBangGameService.queryGameList(pageQueryReq);

        PageQueryResultResp<List<GoRespVO>> pageVo = pageQueryReq.getQueryResult(page.getTotalCount());
        if (pageVo.getTotalCount() > 0) {
            for (GoQueryResp go : page.getData()) {
                pageVo.getData().add(buildGoRespVO(go));
            }
        }
        return JSONObjectResult.create().success(pageVo);
    }

    public GoRespVO buildGoRespVO(GoQueryResp queryResp) {
        GoRespVO goRespVO = new GoRespVO();
        goRespVO.setId(queryResp.getId());
        goRespVO.setBlackUserName(userService.find(queryResp.getBlackUserId()).getUserName());
        goRespVO.setWhiteUserName(userService.find(queryResp.getWhiteUserId()).getUserName());
        goRespVO.setGameName(goRespVO.getBlackUserName() + "和" + goRespVO.getWhiteUserName() + "的对局");
        goRespVO.setGoStatus(queryResp.getGoStatus());
        goRespVO.setEndTime(DateFormatUtils.parseDate(queryResp.getEndTime(), DateFormatEnum.YYYY_MM_DD_HH_MM_SS));
        goRespVO.setGoResult(queryResp.getGoResult());
        goRespVO.setGoType(queryResp.getGoType());
        return goRespVO;
    }

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

        return JSONObjectResult.create().success(placeResultTypeEnum.getDesc());
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
