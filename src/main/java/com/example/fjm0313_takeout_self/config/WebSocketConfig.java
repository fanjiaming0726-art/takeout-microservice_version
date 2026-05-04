package com.example.fjm0313_takeout_self.config;

import com.example.fjm0313_takeout_self.common.MQ.websocket.SellerOrderWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
// 启用原生WebSocket，不然spring不会执行registerWebSocketHandlers方法，执行完该方法后handler自动会被注册路径
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private SellerOrderWebSocketHandler sellerOrderWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(sellerOrderWebSocketHandler,"/ws/seller/orders")
                .setAllowedOriginPatterns("*");
    }

}
