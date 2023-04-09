package com.yhy.demo07.chat.step3groupchat.Handler;

import com.yhy.demo07.chat.session.GroupSessionFactory;
import com.yhy.message.GroupChatRequestMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
@ChannelHandler.Sharable
public class GroupChatChannelInboundHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        List<Channel> channelList = GroupSessionFactory.getGroupSession().getMembersChannel(groupName);
        for (Channel channel : channelList) {
            channel.writeAndFlush(msg);
        }
    }
}
