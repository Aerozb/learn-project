package com.yhy.demo07.chat.step2singlechat.Handler;

import com.yhy.demo07.chat.session.SessionFactory;
import com.yhy.message.ChatRequestMessage;
import com.yhy.message.ChatResponseMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class ChatChannelInboundHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String toUsername = msg.getTo();
        Channel channel = SessionFactory.getSession().getChannel(toUsername);
        if (channel == null) {
            ctx.writeAndFlush(new ChatResponseMessage(false, "对方用户不存在或者不在线"));
            return;
        }
        channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(), msg.getContent()));
    }
}
