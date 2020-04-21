package com.xrc.gb.service.game;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.common.consts.CommonConst;
import com.xrc.gb.common.consts.ErrorInfoConstants;
import com.xrc.gb.common.consts.GoGameConst;
import com.xrc.gb.common.dto.go.GoContext;
import com.xrc.gb.common.dto.go.GoPieces;
import com.xrc.gb.common.dto.go.GoPlaceReq;
import com.xrc.gb.common.dto.go.GoQueryResp;
import com.xrc.gb.common.enums.*;
import com.xrc.gb.common.util.CheckParameter;
import com.xrc.gb.common.util.ExceptionHelper;
import com.xrc.gb.common.util.PageQueryReq;
import com.xrc.gb.common.util.PageQueryResultResp;
import com.xrc.gb.manager.go.GoDataManager;
import com.xrc.gb.manager.go.RoomDataManager;
import com.xrc.gb.repository.dao.GoDAO;
import com.xrc.gb.repository.dao.RoomDAO;
import com.xrc.gb.repository.domain.go.GoDO;
import com.xrc.gb.repository.domain.go.RoomDO;
import com.xrc.gb.work.GoGameFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

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

    @Resource
    private GoDAO goDAO;

    @Override
    public Integer createGame(GoQueryResp goQueryResp) {
        CheckParameter.isNotNull(goQueryResp.getBlackUserId());
        CheckParameter.isNotNull(goQueryResp.getWhiteUserId());

        if (goQueryResp.getGoContext() == null) {
            GoContext goContextCreateReq = new GoContext();
            goContextCreateReq.setCheckerBoardSize(GoGameConst.DEFAULT_GAME_BROAD_SIZE);
            goQueryResp.setGoContext(goContextCreateReq);
        }

        if (goQueryResp.getEndTime() == null) {
            goQueryResp.setEndTime(new Date(System.currentTimeMillis() + GoGameConst.DEFAULT_GAME_TIME_MILLIS));
        }
        if (goQueryResp.getGoType() == null) {
            goQueryResp.setGoType(GameTypeEnum.GO_BANG.getCode());
        }
        goQueryResp.getGoContext().setPlaceArrays(new ArrayList<>());
        goQueryResp.setIsEnd(0);

        GoDO goDO = buildGoDO(goQueryResp);
        if (goDataManager.insert(goDO)) {
            return goDO.getId();
        }
        return null;
    }

    private GoDO buildGoDO(GoQueryResp goQueryResp) {
        GoDO goDO = new GoDO();

        goDO.setId(goQueryResp.getId());
        goDO.setBlackUserId(goQueryResp.getBlackUserId());
        goDO.setWhiteUserId(goQueryResp.getWhiteUserId());
        goDO.setGoContext(JSONObject.toJSONString(goQueryResp.getGoContext()));
        goDO.setIsEnd(goQueryResp.getIsEnd());
        goDO.setEndTime(goQueryResp.getEndTime());
        goDO.setLastUserId(goQueryResp.getLastUserId());
        goDO.setGoStatus(goQueryResp.getGoStatus());
        goDO.setGoType(goQueryResp.getGoType());
        goDO.setGoResult(goQueryResp.getGoResult());
        return goDO;
    }

    @Override
    public GoQueryResp queryGame(Integer id) {
        CheckParameter.isNotNull(id);
        GoDO goDO = goDataManager.queryById(id);
        if (goDO == null) {
            throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_GAME_NOT_EXIST);
        }
        return buildGoQueryResp(goDO);
    }

    private GoQueryResp buildGoQueryResp(GoDO queryGo) {
        GoQueryResp goQuery = new GoQueryResp();

        goQuery.setId(queryGo.getId());
        goQuery.setBlackUserId(queryGo.getBlackUserId());
        goQuery.setWhiteUserId(queryGo.getWhiteUserId());
        goQuery.setGoContext(JSONObject.parseObject(queryGo.getGoContext(), GoContext.class));
        goQuery.setIsEnd(queryGo.getIsEnd());
        goQuery.setEndTime(queryGo.getEndTime());
        goQuery.setLastUserId(queryGo.getLastUserId());
        goQuery.setModifyTime(queryGo.getModifyTime());
        goQuery.setGoStatus(queryGo.getGoStatus());
        goQuery.setGoType(queryGo.getGoType());
        if (queryGo.getGoResult() == null) {
            if (queryGo.getLastUserId() != null) {
                goQuery.setGoResult(queryGo.getLastUserId().equals(queryGo.getBlackUserId()) ? PlaceResultTypeEnum.BLACK_WIN_GAME.getCode() :
                        PlaceResultTypeEnum.WHITE_WIN_GAME.getCode());
            }
        } else {
            goQuery.setGoResult(queryGo.getGoResult());
        }
        return goQuery;
    }

    @Override
    public PageQueryResultResp<List<GoQueryResp>> queryGameList(PageQueryReq<GoQueryResp> pageQueryReq) {
        int count = goDAO.countAll(buildGoDO(pageQueryReq.getData()));
        PageQueryResultResp<List<GoQueryResp>> resultResp = pageQueryReq.getQueryResult(count);
        if (count < 1) {
            return resultResp;
        }
        List<GoDO> goDOS = goDAO.queryAllByLimit(buildGoDO(pageQueryReq.getData()),
                pageQueryReq.getOffSet(), pageQueryReq.getPageSize());
        if (CollectionUtils.isNotEmpty(goDOS)) {
            for (GoDO goDO : goDOS) {
                resultResp.getData().add(buildGoQueryResp(goDO));
            }
        }
        return resultResp;
    }

    /**
     * 通过修改时间判断数据是否被变更
     */
    @Deprecated
    public GoQueryResp queryGameBlock(Integer goId, Long expectModifyTime) {
        CheckParameter.isNotNull(goId);
        CheckParameter.isNotNull(expectModifyTime);

        long deadLine = System.currentTimeMillis() + CommonConst.GO_BANG_REQUEST_BLOCK_TIMES;
        for (; ; ) {
            if (System.currentTimeMillis() > deadLine) {
                throw ExceptionHelper.newBusinessException(ErrorInfoConstants.DATA_FLASH_TIME_OUT);
            }
            GoDO goDO = goDataManager.queryById(goId);
            if (goDO == null) {
                throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_GAME_NOT_EXIST);
            }
            if (goDO.getModifyTime().getTime() > expectModifyTime) {
                return buildGoQueryResp(goDO);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 落子修改
     */
    @Override
    public PlaceResultTypeEnum updateGameForPlace(GoPlaceReq goPlaceReq) {
        CheckParameter.isNotNull(goPlaceReq);
        CheckParameter.isNotNull(goPlaceReq.getId());
        CheckParameter.isNotNull(goPlaceReq.getUserId());
        CheckParameter.isNotNull(goPlaceReq.getGoPieces());

        GoDO goDO = goDataManager.queryById(goPlaceReq.getId());
        if (goDO == null) {
            throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_GAME_NOT_EXIST);
        }
        GoQueryResp goQueryResp = buildGoQueryResp(goDO);
        // 验证用户id和piecesType和用户能否下棋
        if (goPlaceReq.getUserId().equals(goDO.getWhiteUserId())) {
            if (!GoStatusEnum.WHITE_PLACE.getCode().equals(goQueryResp.getGoStatus())) {
                throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_OPERATE_NOT_ALLOW);
            }
            goPlaceReq.getGoPieces().setPieceType(PieceTypeEnum.WHITE_PIECE.getCode());
            goQueryResp.setGoStatus(GoStatusEnum.BLACK_PLACE.getCode());
        } else if (goPlaceReq.getUserId().equals(goDO.getBlackUserId())) {
            if (GoStatusEnum.WHITE_PLACE.getCode().equals(goQueryResp.getGoStatus())) {
                throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_OPERATE_NOT_ALLOW);
            }
            goPlaceReq.getGoPieces().setPieceType(PieceTypeEnum.BLACK_PIECE.getCode());
            goQueryResp.setGoStatus(GoStatusEnum.WHITE_PLACE.getCode());
        } else {
            throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_GAME_NOT_EXIST);
        }

        List<GoPieces> goPiecesList = goQueryResp.getGoContext().getPlaceArrays();
        goPlaceReq.getGoPieces().setHandNum(goPiecesList.size() + 1);
        goPiecesList.add(goPlaceReq.getGoPieces());
        // 落子逻辑运算
        GameTypeEnum gameTypeEnum = GameTypeEnum.getEnum(goQueryResp.getGoType());
        PlaceResultTypeEnum resultTypeEnum = GoGameFactory.subscribe(gameTypeEnum).place(goQueryResp.getGoContext());

        if (!resultTypeEnum.equals(PlaceResultTypeEnum.PLACING_PIECES_SUCCESS)) {
            //  结束房间
            endRoom(goQueryResp);
            goQueryResp.setLastUserId(resultTypeEnum.equals(PlaceResultTypeEnum.BLACK_WIN_GAME) ?
                    goQueryResp.getBlackUserId() : goQueryResp.getWhiteUserId());
        }
        goDataManager.update(buildGoDO(goQueryResp));
        return resultTypeEnum;
    }

    private void endRoom(GoQueryResp goQueryResp) {
        goQueryResp.setGoStatus(GoStatusEnum.END.getCode());
        goQueryResp.setEndTime(new Date());
        goQueryResp.setIsEnd(1);
        endRoom(goQueryResp.getId());
    }

    @Resource
    private RoomDataManager roomDataManager;

    @Resource
    private RoomDAO roomDAO;


    public void endRoom(Integer goId) {
        RoomDO roomDO = roomDAO.queryByGoId(goId);
        RoomDO updateDO = new RoomDO();
        updateDO.setId(roomDO.getId());
        updateDO.setRoomStatus(RoomStatusEnum.ENDED.getCode());
        roomDataManager.update(updateDO);
    }

    public void defeat(Integer goId, Integer userId) {
        CheckParameter.isNotNull(goId);
        CheckParameter.isNotNull(userId);

        GoDO goDO = goDataManager.queryById(goId);
        if (goDO == null) {
            throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_GAME_NOT_EXIST);
        }
        GoDO updateGoDo = new GoDO();
        updateGoDo.setId(goDO.getId());
        updateGoDo.setGoStatus(GoStatusEnum.END.getCode());
        if (goDO.getBlackUserId().equals(userId)) {
            updateGoDo.setGoResult(PlaceResultTypeEnum.WHITE_WIN_GAME.getCode());
        } else if (goDO.getWhiteUserId().equals(userId)) {
            updateGoDo.setGoResult(PlaceResultTypeEnum.BLACK_WIN_GAME.getCode());
        } else {
            throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_YOU_ARE_NOT_IN_ROOM);
        }
        endRoom(goId);
        goDataManager.update(updateGoDo);
    }
}
