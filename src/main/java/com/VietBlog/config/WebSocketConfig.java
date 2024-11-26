package com.VietBlog.config;

import com.VietBlog.handle.FollowStatusWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new FollowStatusWebSocketHandler(), "/ws/follow-status").setAllowedOrigins("*");
    }
    @Bean
    public FollowStatusWebSocketHandler followStatusWebSocketHandler() {
        return new FollowStatusWebSocketHandler();
    }
}
