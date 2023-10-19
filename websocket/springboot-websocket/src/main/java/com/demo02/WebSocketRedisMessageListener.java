package com.demo02;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class WebSocketRedisMessageListener implements MessageListener {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        try {
            // 获取消息
            byte[] messageBody = message.getBody();
            TypeReference<WebSocketMessageDto> reference = new TypeReference<WebSocketMessageDto>() {
            };
            WebSocketMessageDto webSocketMessageDto = Convert.convert(reference, redisTemplate.getValueSerializer().deserialize(messageBody));

            Map<String, WebSocketSession> onlineSessionMap = WebSocketMessageHandler.CLIENTS;
            String toUid = webSocketMessageDto.getToUid();
            if (onlineSessionMap.containsKey(toUid)) {

                String sendUid = webSocketMessageDto.getSendUid();
                String msg = webSocketMessageDto.getMsg();
                log.info("redis监听消息,{} 收到 {} 的消息：{}", sendUid, toUid, msg);
                onlineSessionMap.get(toUid).sendMessage(new TextMessage("收到" + sendUid + "的消息：" + msg));
            }
        } catch (IOException e) {
            log.error("Redis监听消息失败", e);
        }

    }
}
