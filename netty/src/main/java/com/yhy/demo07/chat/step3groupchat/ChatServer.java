package com.yhy.demo07.chat.step3groupchat;

import com.yhy.demo07.chat.common.Const;
import com.yhy.demo07.chat.common.ProcotolFrameDecoder;
import com.yhy.demo07.chat.step3groupchat.Handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatServer {
    private static final LoginRequestMessageChannelInboundHandler LOGIN_HANDLER = new LoginRequestMessageChannelInboundHandler();
    private static final ChatChannelInboundHandler CHAT_HANDLER = new ChatChannelInboundHandler();
    private static final GroupChatChannelInboundHandler GROUP_CHAT_HANDLER = new GroupChatChannelInboundHandler();
    private static final GroupCreateChatChannelInboundHandler GROUP_CREATE_CHAT_HANDLER = new GroupCreateChatChannelInboundHandler();
    private static final GroupJoinRequestMessageHandler GROUP_JOIN_HANDLER = new GroupJoinRequestMessageHandler();
    private static final GroupMembersRequestMessageHandler GROUP_MEMBERS_HANDLER = new GroupMembersRequestMessageHandler();
    private static final GroupQuitRequestMessageHandler GROUP_QUIT_HANDLER = new GroupQuitRequestMessageHandler();

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
                                    .addLast(new IdleStateHandler(5, 0, 0))
                                    // ChannelDuplexHandler 可以同时作为入站和出站处理器
                                    .addLast(new ChannelDuplexHandler() {
                                        // 用来触发特殊事件
                                        @Override
                                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                            IdleStateEvent event = (IdleStateEvent) evt;
                                            // 触发了读空闲事件
                                            if (event.state() == IdleState.READER_IDLE) {
                                                log.info("已经 5s 没有读到数据了");
                                                ctx.channel().close();
                                            }
                                        }
                                    })
                                    //2.根据传进来的消息判断是否是LoginRequestMessage类型
                                    //是的话判断是否登录成功，成功则当前channel和传进来的用户名相互绑定
                                    .addLast(LOGIN_HANDLER)
                                    //3.判断类型是否是发送单聊消息类型ChatRequestMessage，是的话这边服务端负责转发送消息给B客户端
                                    .addLast(CHAT_HANDLER)
                                    //4.判断是否是创建群聊类型
                                    .addLast(GROUP_CREATE_CHAT_HANDLER)
                                    //5.判断是否是加入群聊类型
                                    .addLast(GROUP_JOIN_HANDLER)
                                    //6.判断是否是获取群聊内容类型
                                    .addLast(GROUP_MEMBERS_HANDLER)
                                    //7.判断是否是退出群聊类型
                                    .addLast(GROUP_QUIT_HANDLER)
                                    //8.判断是否是发送群聊消息类型
                                    .addLast(GROUP_CHAT_HANDLER);
                        }
                    }).bind(8080).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("ChatServer异常", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
