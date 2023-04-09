package com.yhy.demo07.chat.step3groupchat;

import com.yhy.demo07.chat.common.Const;
import com.yhy.demo07.chat.common.ProcotolFrameDecoder;
import com.yhy.demo07.chat.step3groupchat.Handler.ClientChannelInboundHandler;
import com.yhy.message.PingMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
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
                                    .addLast(new IdleStateHandler(0, 3, 0))
                                    .addLast(new ChannelDuplexHandler() {
                                        // 用来触发特殊事件
                                        @Override
                                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
                                            IdleStateEvent event = (IdleStateEvent) evt;
                                            // 触发了写空闲事件
                                            if (event.state() == IdleState.WRITER_IDLE) {
//                                                log.info("3s 没有写数据了，发送一个心跳包");
                                                ctx.writeAndFlush(new PingMessage());
                                            }
                                        }
                                    })
                                    .addLast("cilent handler", new ClientChannelInboundHandler());
                        }
                    }).connect("127.0.0.1", 8080).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("ChatClient异常", e);
        } finally {
            group.shutdownGracefully();
        }
    }

}
