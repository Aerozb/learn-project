package com.yhy.demo07.chat.step2singlechat;

import com.yhy.demo07.chat.common.Const;
import com.yhy.demo07.chat.common.ProcotolFrameDecoder;
import com.yhy.demo07.chat.step2singlechat.Handler.ChatChannelInboundHandler;
import com.yhy.demo07.chat.step2singlechat.Handler.LoginRequestMessageChannelInboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServer {
    private static final LoginRequestMessageChannelInboundHandler LOGIN_HANDLER = new LoginRequestMessageChannelInboundHandler();
    private static final ChatChannelInboundHandler CHAT_HANDLER = new ChatChannelInboundHandler();

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            new ServerBootstrap().
                    channel(NioServerSocketChannel.class).
                    group(boss, worker).
                    childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new ProcotolFrameDecoder())
                                    .addLast(Const.loggingHandler)
                                    //1.A客户端发送消息给B客户端，进来先解码然后打印消息
                                    .addLast(Const.messageCodec)
                                    //2.根据传进来的消息判断是否是LoginRequestMessage类型
                                    //是的话判断是否登录成功，成功则当前channel和传进来的用户名相互绑定
                                    .addLast(LOGIN_HANDLER)
                                    //3.判断类型是否是单聊消息类型ChatRequestMessage，是的话这边服务端负责转发送消息给B客户端
                                    .addLast(CHAT_HANDLER);
                        }
                    }).bind(8080).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("ChatServer异常",e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
