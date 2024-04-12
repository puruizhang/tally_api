package com.bestzpr.tally.config;

import com.bestzpr.tally.socket.PayWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * @className WebSocketConfig
 * @Desc webSocket配置
 * @Author 张埔枘
 * @Date 2023/12/8 23:20
 * @Version 1.0
 */
@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(payWebSocketHandler(), "/topic/v1/websocket/room/{roomId}")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }

    /**
     * 支付的Handler
     * @return
     */
    public PayWebSocketHandler payWebSocketHandler() {
        return new PayWebSocketHandler();
    }

}