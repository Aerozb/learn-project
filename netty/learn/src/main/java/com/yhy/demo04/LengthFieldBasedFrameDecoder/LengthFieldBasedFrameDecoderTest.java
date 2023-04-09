package com.yhy.demo04.LengthFieldBasedFrameDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.StandardCharsets;

/**
 * 自定义协议格式
 */
public class LengthFieldBasedFrameDecoderTest {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(1024,0,4,1,0),
                new LoggingHandler(LogLevel.DEBUG)

        );


        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        write(byteBuf,"Hello World");
        write(byteBuf,"Hi");
        channel.writeInbound(byteBuf);
    }

    private static void write(ByteBuf byteBuf,String content) {
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeByte(1);
        byteBuf.writeBytes(bytes);
    }
}
