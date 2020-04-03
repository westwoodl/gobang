package com.xrc.gb.web.controller.game;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.enums.PlaceResultTypeEnum;
import com.xrc.gb.manager.go.dto.GoContext;
import com.xrc.gb.manager.go.dto.GoPieces;
import com.xrc.gb.manager.go.dto.GoPlaceReq;
import com.xrc.gb.manager.go.dto.GoQueryResp;
import com.xrc.gb.repository.domain.go.GoDO;
import com.xrc.gb.service.game.GoBangGameServiceImpl;
import com.xrc.gb.util.PageQueryReq;
import com.xrc.gb.web.common.JSONObjectResult;
import com.xrc.gb.web.controller.AbstractController;
import com.xrc.gb.web.controller.vo.GoBangGameVO;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

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

    /**
     * 单一查询
     */
    @GetMapping("/query")
    public JSONObject query(@RequestParam Integer goId) {
        return JSONObjectResult.create().success(goBangGameService.queryGame(goId));
    }

    @GetMapping("/queryBlock")
    public JSONObject queryBlock(@RequestParam Integer goId, @RequestParam(value = "placeTime") Long expectModifyTime) {
        return JSONObjectResult.create().success(goBangGameService.queryGameBlock(goId, expectModifyTime));
    }

    @PutMapping("/place")
    public JSONObject place(@RequestParam Integer id, @RequestParam Integer x, @RequestParam Integer y) {
        GoPlaceReq goPlaceReq = new GoPlaceReq();
        goPlaceReq.setId(id);
        goPlaceReq.setUserId(getUserId());
        GoPieces goPieces = new GoPieces();
        goPieces.setX(x);
        goPieces.setY(y);
        goPlaceReq.setGoPieces(goPieces);
        PlaceResultTypeEnum placeResultTypeEnum = goBangGameService.updateGameForPlace(goPlaceReq);
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


    @PostMapping
    public JSONObject create(@RequestBody @Validated GoBangGameVO goBangGameVO, BindingResult bindResult) {
        if (bindResult.hasErrors()) {
            JSONObjectResult.create().fail(bindResult.getAllErrors().get(0).getDefaultMessage());
        }
        GoQueryResp goQueryResp = buildGoQueryResp(goBangGameVO);
        if (goBangGameService.createGame(goQueryResp)) {
            return JSONObjectResult.create().success("创建成功");
        } else {
            return JSONObjectResult.create().fail("创建失败");
        }
    }

    private GoQueryResp buildGoQueryResp(GoBangGameVO goBangGameVO) {
        GoQueryResp goQueryResp = new GoQueryResp();
        goQueryResp.setBlackUserId(goBangGameVO.getBlackUserId());
        goQueryResp.setWhiteUserId(goBangGameVO.getWhiteUserId());
        GoContext goContext = new GoContext();
        goContext.setCheckerBoardSize(goBangGameVO.getBoardSize());
//        goQueryResp.setRoomId(goBangGameVO.getRoomId());
        goQueryResp.setEndTime(goBangGameVO.getEndTime());
        return goQueryResp;
    }

}
