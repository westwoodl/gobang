package com.xrc.gb.service.game;

import com.xrc.gb.enums.PlaceResultTypeEnum;
import com.xrc.gb.manager.go.dto.GoPlaceReq;
import com.xrc.gb.manager.go.dto.GoQueryResp;
import com.xrc.gb.util.PageQueryReq;
import com.xrc.gb.util.PageQueryResultResp;

import java.util.List;

/**
 * 预计围棋和五子棋都能实现该接口
 *
 * @author xu rongchao
 * @date 2020/3/27 16:37
 */
public interface GoService {

    boolean createGame(GoQueryResp goQueryResp);

    GoQueryResp queryGame(Integer id);

    PageQueryResultResp<List<GoQueryResp>> queryGameList(PageQueryReq<GoQueryResp> pageQueryReq);

    PlaceResultTypeEnum updateGameForPlace(GoPlaceReq goPlaceReq);

}
