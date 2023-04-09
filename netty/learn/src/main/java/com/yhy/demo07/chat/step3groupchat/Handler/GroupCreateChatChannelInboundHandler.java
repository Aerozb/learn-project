package com.yhy.demo07.chat.step3groupchat.Handler;

import cn.hutool.core.collection.CollUtil;
import com.yhy.demo07.chat.session.GroupSessionFactory;
import com.yhy.message.GroupCreateRequestMessage;
import com.yhy.message.GroupCreateResponseMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

@ChannelHandler.Sharable
public class GroupCreateChatChannelInboundHandler  extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        List<Channel> membersChannel = GroupSessionFactory.getGroupSession().getMembersChannel(groupName);
        if (CollUtil.isEmpty(membersChannel)) {
            GroupSessionFactory.getGroupSession().createGroup(groupName, msg.getMembers());
            ctx.writeAndFlush(new GroupCreateResponseMessage(true,"创建群聊："+groupName+" 成功"));
        }else {
            ctx.writeAndFlush(new GroupCreateResponseMessage(false,"群聊："+groupName+" 已存在"));
        }
    }
}
