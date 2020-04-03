package com.xrc.gb.service.game;

import com.xrc.gb.consts.CommonConst;
import com.xrc.gb.consts.ErrorInfoConstants;
import com.xrc.gb.enums.JoinRoomResultEnum;
import com.xrc.gb.enums.RoomStatusEnum;
import com.xrc.gb.manager.go.GoDataManager;
import com.xrc.gb.manager.go.RoomDataManager;
import com.xrc.gb.repository.dao.UserDAO;
import com.xrc.gb.repository.domain.go.GoDO;
import com.xrc.gb.repository.domain.go.RoomDO;
import com.xrc.gb.repository.domain.user.UserDO;
import com.xrc.gb.util.CheckParameter;
import com.xrc.gb.util.ExceptionHelper;
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

    @Resource
    private UserDAO userDAO;

    @Resource
    private GoDataManager goDataManager;

    /**
     * 通过房间情况 得出用户进入房间的结果
     */
    public JoinRoomResultEnum joinRoom(Integer roomId, UserDO joinUser) {
        CheckParameter.isNotNull(roomId);
        CheckParameter.isNotNull(joinUser);
        CheckParameter.isNotNull(joinUser.getId());

        RoomDO roomDO = roomManager.queryById(roomId);
        if (roomDO == null) {
            throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_ROOM_NOT_EXIST);
        }
        if (joinUser.getId().equals(roomDO.getCreateUser())) {
            JoinRoomResultEnum resultEnum = JoinRoomResultEnum.YOU_ARE_BLACK;
            resultEnum.setExpectModifyTime(roomDO.getModifyTime().getTime());
            return resultEnum;
        }
        if (roomDO.getOpponents() == null || joinUser.getId().equals(roomDO.getOpponents())) {
            roomDO.setOpponents(joinUser.getId());
            roomDO.setRoomNumber(1);
            roomManager.update(roomDO);
            JoinRoomResultEnum resultEnum = JoinRoomResultEnum.YOU_ARE_WHITE;
            resultEnum.setExpectModifyTime(roomManager.queryById(roomId).getModifyTime().getTime());
            return resultEnum;
        }
        roomDO.setRoomNumber(1);
        roomDO.setWatchUser(roomDO.getWatchUser());
        roomManager.update(roomDO);
        JoinRoomResultEnum resultEnum = JoinRoomResultEnum.YOU_ARE_WATCH;
        resultEnum.setExpectModifyTime(roomManager.queryById(roomId).getModifyTime().getTime());
        return resultEnum;
    }

    /**
     * 阻塞查询
     */
    public RoomDO queryBlock(Integer roomId, Long expectModifyTime) {
        CheckParameter.isNotNull(roomId);
        CheckParameter.isNotNull(expectModifyTime);

        long deadLine = System.currentTimeMillis() + CommonConst.GO_BANG_REQUEST_BLOCK_TIMES;
        for (; ; ) {
            if (System.currentTimeMillis() > deadLine) {
                throw ExceptionHelper.newBusinessException(ErrorInfoConstants.DATA_FLASH_TIME_OUT);
            }
            RoomDO roomDO = roomManager.queryById(roomId);
            if (roomDO == null) {
                throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_GAME_NOT_EXIST);
            }
            if (roomDO.getModifyTime().getTime() > expectModifyTime) {
                return roomDO;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开始游戏
     */
    public void startGame(Integer roomId, Integer userId) {
        CheckParameter.isNotNull(roomId);
        CheckParameter.isNotNull(userId);

        RoomDO roomDO = roomManager.queryById(roomId);
        if (roomDO == null) {
            throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_ROOM_NOT_EXIST);
        }
        if (!roomDO.getCreateUser().equals(userId)) {
            throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_OPERATE_NOT_ALLOW);
        }
        if (roomDO.getOpponents() == null ||
                RoomStatusEnum.GAMING.getCode().equals(roomDO.getRoomStatus()) ||
                RoomStatusEnum.ENDED.getCode().equals(roomDO.getRoomStatus())) {
            throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_GAME_START_NOT_ALLOW);
        }
        RoomDO updateReq = new RoomDO();
        updateReq.setId(roomId);
        updateReq.setId(RoomStatusEnum.GAMING.getCode());

        GoDO goDOCreateReq = new GoDO();
        goDOCreateReq.setBlackUserId(roomDO.getCreateUser()); // todo 黑白子被定死了
        goDOCreateReq.setWhiteUserId(roomDO.getOpponents());


        goDataManager.createGo(goDOCreateReq);
    }

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

    public RoomDO queryById(Integer id) {
        CheckParameter.isNotNull(id);
        return roomManager.queryById(id);
    }

}
