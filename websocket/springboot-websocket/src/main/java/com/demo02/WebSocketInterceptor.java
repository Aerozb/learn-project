package com.demo02;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.nio.charset.Charset;
import java.util.Map;

@Slf4j
@Component
public class WebSocketInterceptor implements HandshakeInterceptor {
    /**
     * 握手前
     * @param attributes 如果该方法通过，可以在WebSocketHandler拿到这里设置的数据，org.springframework.web.socket.WebSocketSession#getAttributes()这个可以拿到这里设置的值
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        log.info("uid 握手开始");
        // 获得请求参数
        Map<String, String> paramMap = HttpUtil.decodeParamMap(request.getURI().getQuery(), Charset.defaultCharset());
        String uid = paramMap.get(WebSocketMessageHandler.USER_ID);
        if (CharSequenceUtil.isNotBlank(uid)) {
            // 放入属性域
            attributes.put(WebSocketMessageHandler.USER_ID, uid);
            log.info("用户{}握手成功！", uid);
            return true;
        }
        return false;
    }

    /**
     * 握手后
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("握手完成");
    }

}
