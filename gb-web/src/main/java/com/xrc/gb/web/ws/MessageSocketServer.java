package com.xrc.gb.web.ws;

import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.common.enums.MessageTypeEnum;
import com.xrc.gb.repository.domain.user.UserDO;
import com.xrc.gb.service.user.UserService;
import com.xrc.gb.web.common.JSONObjectResult;
import com.xrc.gb.web.controller.vo.RoomGameVO;
import com.xrc.gb.web.ws.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xu rongchao
 * @date 2020/4/18 9:27
 */
@ServerEndpoint("/socket/square/message/{userId}")
@Component
@Slf4j
public class MessageSocketServer {

    private static final ConcurrentHashMap<Integer, Session> webSocketMap = new ConcurrentHashMap<>();

//    private final AtomicInteger onlineNum = new AtomicInteger(0);

    public static UserService userService;
//            ContextLoader.getCurrentWebApplicationContext().getBean("userServiceImpl", UserService.class);

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId) {
        if (session != null && userId != null) {
            UserDO userDO = userService.find(userId);
            if(userDO != null) {
                webSocketMap.put(userId, session);
                log.info("聊天用户连接:" + userId + ",online num:" + webSocketMap.size());
                sendAll(new MessageVO(webSocketMap.size(), System.currentTimeMillis(), null, userDO.getUserName() + "进入房间", MessageTypeEnum.JOIN_ROOM.getType()));
            }
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userId") Integer userId) {
        if (userId != null) {
            webSocketMap.remove(userId);
            log.info("聊天用户退出:" + userId + ",online num:" + webSocketMap.size());
            sendAll(new MessageVO(webSocketMap.size(), System.currentTimeMillis(), null,
                    userService.find(userId).getUserName() + "离开房间", MessageTypeEnum.LEAVE_ROOM.getType()));
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(@PathParam("userId") Integer userId, String message) {
        if (userId != null && StringUtils.isNotBlank(message)) {
            UserDO userDO = userService.find(userId);
            if (userDO == null) {
                webSocketMap.remove(userId);
            } else {
                sendAll(new MessageVO(webSocketMap.size(), System.currentTimeMillis(), userDO, message, MessageTypeEnum.SEND_MESSAGE.getType()));
            }
        }
    }

    @OnError
    public void onError(@PathParam("userId") Integer userId, Throwable error) {
        log.error("用户错误:" + userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 发送自定义消息
     */
    public static void sendMessage(String message, Integer... userIdArray) {
        if (StringUtils.isBlank(message) || userIdArray == null || userIdArray.length == 0) {
            return;
        }
        for (Integer userId : userIdArray) {
            if (userId != null) {
                Session session = webSocketMap.get(userId);
                if (session != null) {
                    session.getAsyncRemote().sendText(message);
                }
            }
        }
    }

    public static void sendAll(MessageVO messageVO) {
        for (Map.Entry<Integer, Session> e : webSocketMap.entrySet()) {
            Session session = e.getValue();
            if (session != null) {
                if (MessageTypeEnum.SEND_MESSAGE.getType().equals(messageVO.getMessageType())) {
                    if (!e.getKey().equals(messageVO.getUserDO().getId())) {
                        session.getAsyncRemote().sendText(JSONObject.toJSONString(messageVO));
                    }
                    continue;
                }
                if (MessageTypeEnum.JOIN_ROOM.getType().equals(messageVO.getMessageType()) ||
                        MessageTypeEnum.LEAVE_ROOM.getType().equals(messageVO.getMessageType())) {
                    session.getAsyncRemote().sendText(JSONObject.toJSONString(messageVO));
                }
            }
        }
    }

    /**
     * 发送实体
     */
    public static void send(RoomGameVO roomGameVO, Integer... userIdArray) {
        sendMessage(JSONObjectResult.create().success(roomGameVO).toJSONString(), userIdArray);
    }

}
