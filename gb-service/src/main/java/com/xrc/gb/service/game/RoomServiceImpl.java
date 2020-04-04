package com.xrc.gb.service.game;

import com.xrc.gb.consts.CommonConst;
import com.xrc.gb.consts.ErrorInfoConstants;
import com.xrc.gb.enums.JoinRoomResultEnum;
import com.xrc.gb.enums.RoomStatusEnum;
import com.xrc.gb.manager.go.GoDataManager;
import com.xrc.gb.manager.go.RoomDataManager;
import com.xrc.gb.manager.go.UserDataManager;
import com.xrc.gb.repository.domain.go.GoDO;
import com.xrc.gb.repository.domain.go.RoomDO;
import com.xrc.gb.repository.domain.user.UserDO;
import com.xrc.gb.util.CheckParameter;
import com.xrc.gb.util.ExceptionHelper;
import com.xrc.gb.util.PageQueryReq;
import com.xrc.gb.util.PageQueryResultResp;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.Roman;
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
    private UserDataManager userDataManager;

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
        RoomDO roomUpdateReq = new RoomDO();
        roomUpdateReq.setId(roomId);
        if (roomDO == null) {
            throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_ROOM_NOT_EXIST);
        }
        if (joinUser.getId().equals(roomDO.getCreateUser())) {
            JoinRoomResultEnum resultEnum = JoinRoomResultEnum.YOU_ARE_BLACK;
            resultEnum.setExpectModifyTime(roomDO.getModifyTime().getTime());
            return resultEnum;
        }
        if (roomDO.getOpponents() == null || joinUser.getId().equals(roomDO.getOpponents())) {
            if (roomDO.getOpponents() == null) {
                roomUpdateReq.setRoomNumber(1);
            }
            roomUpdateReq.setOpponents(joinUser.getId());
            roomManager.update(roomUpdateReq);
            JoinRoomResultEnum resultEnum = JoinRoomResultEnum.YOU_ARE_WHITE;
            resultEnum.setExpectModifyTime(roomManager.queryById(roomId).getModifyTime().getTime());
            return resultEnum;
        }
        roomUpdateReq.setRoomNumber(1);
        roomUpdateReq.setWatchUser(roomDO.getWatchUser());
        roomManager.update(roomUpdateReq);
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
    public boolean startGame(Integer roomId, Integer userId) {
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
        return true;
//
//        GoDO goDOCreateReq = new GoDO();
//        goDOCreateReq.setBlackUserId(roomDO.getCreateUser()); // todo 黑白子被定死了
//        goDOCreateReq.setWhiteUserId(roomDO.getOpponents());
//        goDOCreateReq.setGoContext();
//
//        return goDataManager.createGo(goDOCreateReq);
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

//    public boolean delete(int roomId) {
//        return roomManager.deleteById(roomId);
//    }

    public RoomDO queryById(Integer id) {
        CheckParameter.isNotNull(id);
        return roomManager.queryById(id);
    }

    public boolean leaveRoom(Integer roomId, Integer userId) {
        CheckParameter.isNotNull(roomId);
        CheckParameter.isNotNull(userId);
        RoomDO roomDO = roomManager.queryById(roomId);
        if (roomDO.getRoomNumber() <= 0) {
            throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_YOU_ARE_NOT_IN_ROOM);
        }
        if (roomDO.getCreateUser().equals(userId)) {
            return roomManager.deleteById(roomId);
        }
        RoomDO updateRoom = new RoomDO();
        if (roomDO.getOpponents().equals(userId)) {
            updateRoom.setId(roomId);
            updateRoom.setRoomNumber(-1);
            updateRoom.setOpponents(0);
            return roomManager.update(roomDO);
        }
        if (StringUtils.isBlank(roomDO.getWatchUser())) {
            throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_YOU_ARE_NOT_IN_ROOM);
        }
        // todo 优化为分布式锁 lock room id
        synchronized (RoomServiceImpl.class) {
            StringBuilder wUserSb = new StringBuilder();
            updateRoom.setId(roomId);
            boolean isIn = false;
            for (String wId : roomManager.queryById(roomId).getWatchUser().split(CommonConst.SPLIT_STR)) {
                if (StringUtils.isBlank(wId)) {
                    continue;
                }
                if (wId.equals(String.valueOf(userId))) {
                    updateRoom.setRoomNumber(-1);
                    isIn = true;
                } else {
                    wUserSb.append(wId).append(CommonConst.SPLIT_STR);
                }
            }
            if (!isIn) {
                throw ExceptionHelper.newBusinessException(ErrorInfoConstants.BIZ_YOU_ARE_NOT_IN_ROOM);
            }
            updateRoom.setWatchUser(wUserSb.toString());
            return roomManager.update(updateRoom);
        }
    }
}
