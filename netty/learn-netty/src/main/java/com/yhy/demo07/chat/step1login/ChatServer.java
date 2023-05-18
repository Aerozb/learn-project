package com.yhy.demo07.chat.step1login;

import com.yhy.demo07.chat.common.ProcotolFrameDecoder;
import com.yhy.demo07.chat.service.UserServiceFactory;
import com.yhy.demo07.chat.common.Const;
import com.yhy.message.LoginRequestMessage;
import com.yhy.message.LoginResponseMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChatServer {
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
                                    .addLast(Const.messageCodec)
                                    .addLast(new SimpleChannelInboundHandler<LoginRequestMessage>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
                                            String username = msg.getUsername();
                                            String password = msg.getPassword();
                                            boolean login = UserServiceFactory.getUserService().login(username, password);
                                            LoginResponseMessage message;
                                            if (login) {
                                                message = new LoginResponseMessage(true, "登录成功");
                                            } else {
                                                message = new LoginResponseMessage(false, "用户名或密码不正确");
                                            }
                                            ctx.writeAndFlush(message);
                                        }
                                    });
                        }
                    }).bind(8080).sync().channel().closeFuture().sync();

        } catch (InterruptedException e) {
            System.out.println(e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
