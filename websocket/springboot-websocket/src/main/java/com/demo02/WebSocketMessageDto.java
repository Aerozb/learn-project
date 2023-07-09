package com.demo02;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessageDto implements Serializable {

    public static void main(String[] args) {
        WebSocketMessageDto webSocketMessageDto = new WebSocketMessageDto();
        webSocketMessageDto.setToUid("3");
        webSocketMessageDto.setSendUid("1");
        webSocketMessageDto.setMsg("牛逼");
        System.out.println(JSON.toJSONString(webSocketMessageDto));
    }

    private static final long serialVersionUID = -4291728346293647762L;

    private String toUid;
    private String sendUid;
    private String msg;
}
