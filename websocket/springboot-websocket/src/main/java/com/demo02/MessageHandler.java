package com.demo02;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MessageHandler extends TextWebSocketHandler {

    /**
     * redis 订阅通道名
     */
    public static final String CHANNEL_NAME = "msgRedisTopic";
    /**
     * 当前节点在线session
     */
    public static Map<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<>();
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String uid = String.valueOf(session.getAttributes().get("uid"));
        CLIENTS.put(uid, session);
        log.info("uri :" + session.getUri());
        log.info("连接建立:uid{} ", uid);
        log.info("当前连接服务器客户端数: {}", CLIENTS.size());
        log.info("===================================");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String uid = String.valueOf(session.getAttributes().get("uid"));
        CLIENTS.remove(uid);
        log.info("断开连接: uid{}", uid);
        log.info("当前连接服务器客户端数: {}", CLIENTS.size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        log.info("服务端收到消息：{}", payload);
        boolean isValid = JSON.isValidObject(payload);
        if (!isValid) {
            log.info("服务端收到消息的数据格式不符合要求");
            return;
        }
        MessageDto messageDto = JSONUtil.toBean(payload, MessageDto.class);
        String toUid = messageDto.getToUid();

        if (CLIENTS.containsKey(toUid)) {
            try {
                log.info("当前ws服务器内包含客户端uid {},直接发送消息", toUid);
                CLIENTS.get(toUid).sendMessage(new TextMessage("收到" + messageDto.getSendUid() + "的信息:" + payload));
            } catch (Exception e) {
                log.error("发送消息给uid:{}失败", toUid, e);
            }
        } else {
            log.warn("当前ws服务器内未找到客户端uid {}，推送到redis", toUid);
            // 发布消息
            redisTemplate.convertAndSend(CHANNEL_NAME, messageDto);
        }
    }
}
