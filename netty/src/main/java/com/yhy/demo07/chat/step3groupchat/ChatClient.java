package com.yhy.demo07.chat.step3groupchat;

import com.yhy.demo07.chat.common.Const;
import com.yhy.demo07.chat.common.ProcotolFrameDecoder;
import com.yhy.demo07.chat.step3groupchat.Handler.ClientChannelInboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            new Bootstrap().
                    channel(NioSocketChannel.class).
                    group(group).
                    handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ProcotolFrameDecoder())
                                    .addLast(Const.loggingHandler)
                                    .addLast(Const.messageCodec)
                                    .addLast("cilent handler", new ClientChannelInboundHandler());
                        }
                    }).connect("127.0.0.1", 8080).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("ChatClient异常",e);
        } finally {
            group.shutdownGracefully();
        }
    }

}
