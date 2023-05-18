package com.yhy.demo07.chat.step1login;

import cn.hutool.core.thread.ThreadUtil;
import com.yhy.demo07.chat.common.ProcotolFrameDecoder;
import com.yhy.demo07.chat.common.Const;
import com.yhy.message.LoginRequestMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;


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
                                    .addLast("cilent handler", new ChannelInboundHandlerAdapter() {

                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            ThreadUtil.newExecutor().execute(() -> {
                                                Scanner scanner = new Scanner(System.in);
                                                System.out.println("请输入用户名:");
                                                String username = scanner.nextLine();
                                                System.out.println("请输入密码:");
                                                String password = scanner.nextLine();
                                                // 构造消息对象
                                                LoginRequestMessage message = new LoginRequestMessage(username, password);
                                                // 发送消息
                                                ctx.writeAndFlush(message);
                                            });
                                        }
                                    });
                        }
                    }).connect("127.0.0.1", 8080).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println(e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
