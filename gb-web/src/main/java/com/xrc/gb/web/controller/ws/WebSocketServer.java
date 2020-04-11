package com.xrc.gb.web.controller.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xrc.gb.service.game.RoomServiceImpl;
import com.xrc.gb.web.common.JSONObjectResult;
import com.xrc.gb.web.common.OnlineCountUtils;
import com.xrc.gb.web.controller.vo.RoomGameVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xu rongchao
 * @date 2020/4/7 14:12
 */
@ServerEndpoint("/socket/go/{userId}")
@Component
@Slf4j
public class WebSocketServer {
    private static ConcurrentHashMap<Integer, Session> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId) {
        webSocketMap.put(userId, session);
        OnlineCountUtils.addOnlineCount();
        webSocketMap.size();
        log.info("用户连接:" + userId + ",当前在线人数为:" + OnlineCountUtils.getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userId") Integer userId) {
        webSocketMap.remove(userId);
        OnlineCountUtils.subOnlineCount();
        log.info("用户退出:" + userId + ",当前在线人数为:" + OnlineCountUtils.getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(@PathParam("userId") Integer userId, String message) {
        log.info("用户消息:" + userId + ",报文:" + message);

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
        if (StringUtils.isBlank(message)) {
            return;
        }
        if (userIdArray != null && userIdArray.length > 0) {
            for (Integer userId : userIdArray) {
                Session session = webSocketMap.get(userId);
                if (session != null) {
                    session.getAsyncRemote().sendText(message);
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
