package com.kevin.dinofly.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * const userId = '1'; // 假设当前用户ID是 1
 * const socket = new WebSocket('ws://localhost:8080/chat?userId=' + userId);
 *
 *
 * const targetUserId = '2'; // 假设目标用户ID是 2
 * const messageContent = 'Hello, I am interested in your apartment.';
 * const message = targetUserId + ':' + messageContent;
 * socket.send(message);
 */
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = getUserId(session); // 获取用户ID的方法
        if (userId != null) {
            sessions.put(userId, session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        // 假设消息格式为 "targetUserId:messageContent"
        String[] parts = payload.split(":", 2);
        if (parts.length == 2) {
            String targetUserId = parts[0];
            String messageContent = parts[1];
            WebSocketSession targetSession = sessions.get(targetUserId);
            if (targetSession != null && targetSession.isOpen()) {
                targetSession.sendMessage(new TextMessage(messageContent));
                // 在发送消息时，通知目标用户
                sendNotification(targetUserId, getUserId(session), "You have a new message");
            }
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = getUserId(session);
        if (userId != null) {
            sessions.remove(userId);
        }
    }

    private String getUserId(WebSocketSession session) {
        // 从 WebSocket 会话中获取用户ID的方法，具体实现取决于你的应用逻辑
        // 例如，可以从 session 的属性或查询参数中获取用户ID
        return (String) session.getAttributes().get("userId");
    }

    public void sendNotification(String userId, String senderUserId, String message) throws IOException {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            String notificationMessage = senderUserId + " sent you a message: " + message;
            session.sendMessage(new TextMessage(notificationMessage));
        }
    }




}

