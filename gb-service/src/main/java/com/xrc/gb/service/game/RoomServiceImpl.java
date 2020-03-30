package com.xrc.gb.service.game;

import com.xrc.gb.manager.go.RoomDataManager;
import com.xrc.gb.repository.domain.go.RoomDO;
import com.xrc.gb.util.CheckParameter;
import com.xrc.gb.util.PageQueryReq;
import com.xrc.gb.util.PageQueryResultResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xu rongchao
 * @date 2020/3/28 15:58
 */
@Service
public class RoomServiceImpl {
    @Resource
    private RoomDataManager roomManager;


    public PageQueryResultResp<List<RoomDO>> queryPage(PageQueryReq<RoomDO> pageQueryReq) {
        CheckParameter.isNotNull(pageQueryReq);
        CheckParameter.isNotNull(pageQueryReq.getData());

        return roomManager.queryPage(pageQueryReq);
    }

    public boolean create(RoomDO roomDO) {
        CheckParameter.isNotNull(roomDO);
        CheckParameter.isNotNull(roomDO.getRoomName());
        CheckParameter.isNotNull(roomDO.getCreateUser());
        CheckParameter.isNotNull(roomDO.getRoomStatus());
        return roomManager.createRoom(roomDO);
    }

    public boolean update(RoomDO roomDO) {
        CheckParameter.isNotNull(roomDO);
        CheckParameter.isNotNull(roomDO.getId());
        return roomManager.update(roomDO);
    }

    public boolean delete(int roomId) {
        return roomManager.deleteById(roomId);
    }
}
