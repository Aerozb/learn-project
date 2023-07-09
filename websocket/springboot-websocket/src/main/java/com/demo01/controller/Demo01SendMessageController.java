package com.demo01.controller;

import com.demo01.websocket.WebsocketSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@RestController
@RequestMapping
@Slf4j
public class Demo01SendMessageController {

    @RequestMapping("/sendAllMessage")
    public void sendAllMessage(String message) throws IOException {
        for (WebSocketSession webSocketSession : WebsocketSessionManager.getAll()) {
            log.info("id:{}",webSocketSession.getId());
            webSocketSession.sendMessage(new TextMessage(message));
        }
    }
}
