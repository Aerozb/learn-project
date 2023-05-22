package com.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;

@Slf4j
@Component
public class HttpAuthHandler extends TextWebSocketHandler {

    private static final String SESSION_ID = "session_id";

    /**
     * socket 建立成功事件
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Object sessionId = session.getAttributes().get(SESSION_ID);
        if (sessionId != null) {
            // 用户连接成功，放入在线用户缓存
            WsSessionManager.add(sessionId.toString(), session);
        } else {
            throw new RuntimeException("用户登录已经失效!");
        }
    }

    /**
     * 接收消息事件
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 获得客户端传来的消息
        String payload = message.getPayload();
        Object sessionId = session.getAttributes().get(SESSION_ID);
        log.info("server 接收到 {} 发送的 {}", sessionId, payload);
        session.sendMessage(new TextMessage("server 发送给 " + sessionId + " 消息 " + payload + " " + LocalDateTime.now()));
    }

    /**
     * socket 断开连接时
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Object sessionId = session.getAttributes().get(SESSION_ID);
        if (sessionId != null) {
            // 用户退出，移除缓存
            WsSessionManager.remove(sessionId.toString());
        }
    }

}

