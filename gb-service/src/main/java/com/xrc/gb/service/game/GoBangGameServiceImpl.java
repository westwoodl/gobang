package com.xrc.gb.service.game;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.consts.ErrorInfoConstants;
import com.xrc.gb.consts.GoGameConst;
import com.xrc.gb.enums.GameTypeEnum;
import com.xrc.gb.enums.PieceTypeEnum;
import com.xrc.gb.enums.PlaceResultTypeEnum;
import com.xrc.gb.manager.go.GoDataManager;
import com.xrc.gb.manager.go.dto.GoContext;
import com.xrc.gb.manager.go.dto.GoPieces;
import com.xrc.gb.manager.go.dto.GoPlaceReq;
import com.xrc.gb.manager.go.dto.GoQueryResp;
import com.xrc.gb.repository.domain.go.GoDO;
import com.xrc.gb.util.CheckParameter;
import com.xrc.gb.util.ExceptionHelper;
import com.xrc.gb.util.PageQueryReq;
import com.xrc.gb.util.PageQueryResultResp;
import com.xrc.gb.work.GoGameFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xu rongchao
 * @date 2020/3/27 16:45
 */
@Service
public class GoBangGameServiceImpl implements GoService {
    @Resource
    private GoDataManager goDataManager;

    @Override
    public boolean createGame(GoQueryResp goQueryResp) {
        CheckParameter.isNotNull(goQueryResp.getBlackUserId());
        CheckParameter.isNotNull(goQueryResp.getWhiteUserId());
        CheckParameter.isNotNull(goQueryResp.getGoContext());
        CheckParameter.isNotNull(goQueryResp.getGoContext().getCheckerBoardSize());

        if (goQueryResp.getEndTime() == null) {
            goQueryResp.setEndTime(new Date(System.currentTimeMillis() + GoGameConst.DEFAULT_GAME_TIME_MILLIS));
        }
        goQueryResp.getGoContext().setPlaceArrays(new ArrayList<>());
        goQueryResp.setIsEnd(0);
        GoDO goDO = goDataManager.createGo(buildGoDO(goQueryResp));
        return goDO != null;
    }

    private GoDO buildGoDO(GoQueryResp goQueryResp) {
        GoDO goDO = new GoDO();

        goDO.setId(goQueryResp.getId());
        goDO.setBlackUserId(goQueryResp.getBlackUserId());
        goDO.setWhiteUserId(goQueryResp.getWhiteUserId());
        goDO.setGoContext(JSONObject.toJSONString(goQueryResp.getGoContext()));
//        goDO.setRoomId(goQueryResp.getRoomId());
        goDO.setIsEnd(goQueryResp.getIsEnd());
        goDO.setEndTime(goQueryResp.getEndTime());
        goDO.setLastUserId(goQueryResp.getLastUserId());
        return goDO;
    }

    @Override
    public GoQueryResp queryGame(Integer id) {
        return buildGoQueryResp(goDataManager.queryGo(id));
    }

    private GoQueryResp buildGoQueryResp(GoDO queryGo) {
        GoQueryResp goQuery = new GoQueryResp();

        goQuery.setBlackUserId(queryGo.getBlackUserId());
        goQuery.setWhiteUserId(queryGo.getWhiteUserId());
        goQuery.setGoContext(JSONObject.parseObject(queryGo.getGoContext(), GoContext.class));
//        goQuery.setRoomId(queryGo.getRoomId());
        goQuery.setIsEnd(queryGo.getIsEnd());
        goQuery.setEndTime(queryGo.getEndTime());
        goQuery.setLastUserId(queryGo.getLastUserId());
        return goQuery;
    }

    @Override
    public PageQueryResultResp<GoQueryResp> queryGameList(PageQueryReq<GoQueryResp> pageQueryReq) {
        return null;
    }

    /**
     *
     */
    @Override
    public PlaceResultTypeEnum updateGameForPlace(GoPlaceReq goPlaceReq) {
        CheckParameter.isNotNull(goPlaceReq);
        CheckParameter.isNotNull(goPlaceReq.getId());
        CheckParameter.isNotNull(goPlaceReq.getUserId());
        CheckParameter.isNotNull(goPlaceReq.getGoPieces());

        GoDO goDO = goDataManager.queryGo(goPlaceReq.getId());
        if (goDO == null) {
            throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_GAME_NOT_EXIST);
        }
        GoQueryResp goQueryResp = buildGoQueryResp(goDO);
        // 验证用户id和piecesType
        if (goPlaceReq.getUserId().equals(goDO.getWhiteUserId())) {
            goPlaceReq.getGoPieces().setPieceType(PieceTypeEnum.WHITE_PIECE.getCode());
        } else if (goPlaceReq.getUserId().equals(goDO.getBlackUserId())) {
            goPlaceReq.getGoPieces().setPieceType(PieceTypeEnum.BLACK_PIECE.getCode());
        } else {
            throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_GAME_NOT_EXIST);
        }

        List<GoPieces> goPiecesList = goQueryResp.getGoContext().getPlaceArrays();
        goPlaceReq.getGoPieces().setHandNum(goPiecesList.size() + 1);
        goPiecesList.add(goPlaceReq.getGoPieces());

        PlaceResultTypeEnum resultTypeEnum = GoGameFactory.subscribe(GameTypeEnum.GO_BANG).place(goQueryResp.getGoContext());
        if (goDataManager.updateGo(buildGoDO(goQueryResp))) {
            return resultTypeEnum;
        } else {
            throw ExceptionHelper.newSysException(ErrorInfoConstants.BIZ_SYSTEM_BUSY);
        }
    }
}
