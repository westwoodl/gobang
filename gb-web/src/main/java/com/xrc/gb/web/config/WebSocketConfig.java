package com.xrc.gb.web.config;

import com.xrc.gb.service.user.UserService;
import com.xrc.gb.web.ws.MessageSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 开启WebSocket支持
 *
 * @author xu rongchao
 * @date 2020/4/7 14:11
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


    @Autowired
    public void setMessageSocketUserService(UserService userService) {
        MessageSocketServer.userService = userService;
    }
}
