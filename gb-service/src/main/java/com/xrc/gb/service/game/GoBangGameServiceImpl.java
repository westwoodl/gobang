package com.xrc.gb.service.game;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.consts.GoGameConst;
import com.xrc.gb.manager.go.GoCacheManager;
import com.xrc.gb.manager.go.dto.GoContext;
import com.xrc.gb.manager.go.dto.GoQueryResp;
import com.xrc.gb.repository.dao.GoDAO;
import com.xrc.gb.repository.domain.go.GoDO;
import com.xrc.gb.util.CheckParameter;
import com.xrc.gb.util.PageQueryReq;
import com.xrc.gb.util.PageQueryResultResp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author xu rongchao
 * @date 2020/3/27 16:45
 */
@Service
@Transactional
public class GoBangGameServiceImpl implements GoService {
    @Resource
    private GoCacheManager goCacheManager;

    @Override
    public boolean createGame(GoQueryResp goQueryResp) {
        CheckParameter.isNotNull(goQueryResp.getBlackUserId());
        CheckParameter.isNotNull(goQueryResp.getWhiteUserId());
        CheckParameter.isNotNull(goQueryResp.getRoomId());
        CheckParameter.isNotNull(goQueryResp.getGoContext());
        CheckParameter.isNotNull(goQueryResp.getGoContext().getCheckerBoardSize());

        if (goQueryResp.getEndTime() == null) {
            goQueryResp.setEndTime(new Date(System.currentTimeMillis() + GoGameConst.DEFAULT_GAME_TIME_MILLIS));
        }
        goQueryResp.getGoContext().setPlaceArrays(new ArrayList<>());
        goQueryResp.setIsEnd(0);
        GoDO goDO = goCacheManager.createGo(buildGoDO(goQueryResp));
        return goDO != null;
    }

    private GoDO buildGoDO(GoQueryResp goQueryResp) {
        GoDO goDO = new GoDO();

        goDO.setId(goQueryResp.getId());
        goDO.setBlackUserId(goQueryResp.getBlackUserId());
        goDO.setWhiteUserId(goQueryResp.getWhiteUserId());
        goDO.setGoContext(JSONObject.toJSONString(goQueryResp.getGoContext()));
        goDO.setRoomId(goQueryResp.getRoomId());
        goDO.setIsEnd(goQueryResp.getIsEnd());
        goDO.setEndTime(goQueryResp.getEndTime());
        goDO.setLastUserId(goQueryResp.getLastUserId());
        return goDO;
    }

    @Override
    public GoQueryResp queryGame(Integer id) {
        return buildGoQueryResp(goCacheManager.queryGo(id));
    }

    private GoQueryResp buildGoQueryResp(GoDO queryGo) {
        GoQueryResp goQuery = new GoQueryResp();

        goQuery.setBlackUserId(queryGo.getBlackUserId());
        goQuery.setWhiteUserId(queryGo.getWhiteUserId());
        goQuery.setGoContext(JSONObject.parseObject(queryGo.getGoContext(), GoContext.class));
        goQuery.setRoomId(queryGo.getRoomId());
        goQuery.setIsEnd(queryGo.getIsEnd());
        goQuery.setEndTime(queryGo.getEndTime());
        goQuery.setLastUserId(queryGo.getLastUserId());
        return goQuery;
    }

    @Override
    public PageQueryResultResp<GoQueryResp> queryGameList(PageQueryReq<GoQueryResp> pageQueryReq) {
        return null;
    }

    @Override
    public boolean updateGame(GoQueryResp goQueryResp) {
        CheckParameter.isNotNull(goQueryResp);
        CheckParameter.isNotNull(goQueryResp.getId());

        return goCacheManager.updateGo(buildGoDO(goQueryResp));
    }
}
