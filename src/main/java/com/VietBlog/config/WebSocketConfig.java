package com.VietBlog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.AbstractWebSocketMessage;
import org.springframework.web.socket.config.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // Endpoint cho WebSocket
                .setAllowedOriginPatterns("*") // Cho phép tất cả các nguồn (frontend/backend khác domain)
                .withSockJS(); // Kích hoạt SockJS
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // Đích đến cho các tin nhắn gửi đi
        registry.setApplicationDestinationPrefixes("/app"); // Đích đến cho các tin nhắn từ client
    }
}
