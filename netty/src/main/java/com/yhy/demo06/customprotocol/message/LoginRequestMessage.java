package com.yhy.demo06.customprotocol.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString(callSuper = true)
public class LoginRequestMessage extends Message {
    private String username;
    private String password;

    private String nickname;

    @Override
    public int getMessageType() {
        return LoginRequestMessage;
    }
}
