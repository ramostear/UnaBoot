package com.ramostear.unaboot.configuration;

import com.ramostear.unaboot.web.admin.LoggerWsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @ClassName WebSocketConfiguration
 * @Description TODO
 * @Author 树下魅狐
 * @Date 2020/2/17 0017 6:22
 * @Version since UnaBoot-1.0
 **/
@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    @Autowired
    private LoggerWsHandler loggerWsHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(loggerWsHandler,"/admin/websocket/logging").setAllowedOrigins("*");
    }
}
