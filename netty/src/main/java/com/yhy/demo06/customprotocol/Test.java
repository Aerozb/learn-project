package com.yhy.demo06.customprotocol;

import com.yhy.message.LoginRequestMessage;
import com.yhy.demo06.customprotocol.nosharable.MessageCodec_and_test;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Test {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(new LoggingHandler(LogLevel.DEBUG), new MessageCodec_and_test());
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "123456", "张三");
        //入栈编码，出栈解码
        channel.writeOutbound(message);
    }
}
