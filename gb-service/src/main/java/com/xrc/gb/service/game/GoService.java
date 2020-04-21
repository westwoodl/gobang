package com.xrc.gb.service.game;

import com.xrc.gb.common.enums.PlaceResultTypeEnum;
import com.xrc.gb.common.dto.go.GoPlaceReq;
import com.xrc.gb.common.dto.go.GoQueryResp;
import com.xrc.gb.common.util.PageQueryReq;
import com.xrc.gb.common.util.PageQueryResultResp;

import java.util.List;

/**
 * 预计围棋和五子棋都能实现该接口
 *
 * @author xu rongchao
 * @date 2020/3/27 16:37
 */
public interface GoService {

    /**
     * 返回 id
     */
    Integer createGame(GoQueryResp goQueryResp);

    GoQueryResp queryGame(Integer id);

    PageQueryResultResp<List<GoQueryResp>> queryGameList(PageQueryReq<GoQueryResp> pageQueryReq);

    PlaceResultTypeEnum updateGameForPlace(GoPlaceReq goPlaceReq);

}
