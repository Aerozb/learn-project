package com.demo02;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto implements Serializable {

    public static void main(String[] args) {
        MessageDto messageDto = new MessageDto();
        messageDto.setToUid("3");
        messageDto.setSendUid("1");
        messageDto.setMsg("牛逼");
        System.out.println(JSON.toJSONString(messageDto));
    }

    private static final long serialVersionUID = -4291728346293647762L;

    private String toUid;
    private String sendUid;
    private String msg;
}
