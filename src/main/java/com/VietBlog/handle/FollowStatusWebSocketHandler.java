package com.VietBlog.handle;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FollowStatusWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Nếu cần xử lý thông điệp từ client, viết logic ở đây
    }

    // Phương thức gửi trạng thái follow thay đổi đến tất cả các client
    public void sendFollowStatusChange(Long userId, Boolean isFollowing) {
        String message = String.format("{\"userId\": %d, \"isFollowing\": %b}", userId, isFollowing);
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendBlockStatusChange(Long userId, Boolean isBlocking) {
        String message = String.format("{\"userId\": %d, \"isBlocking\": %b}", userId, isBlocking);
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
