package com.yhy.demo06.customprotocol;

import com.yhy.demo06.customprotocol.message.LoginRequestMessage;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Test {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(new LoggingHandler(LogLevel.DEBUG), new MessageCodec());
        LoginRequestMessage loginRequestMessage = new LoginRequestMessage("zhangsan", "123456", "张三");
        //入栈编码，出栈解码
        channel.writeOutbound(loginRequestMessage);
    }
}
