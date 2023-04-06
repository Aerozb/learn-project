package com.yhy.demo07.chat.step3groupchat.Handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import com.yhy.demo07.chat.common.Const;
import com.yhy.message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public
class ClientChannelInboundHandler extends ChannelInboundHandlerAdapter {

    private static final CountDownLatch WAIT_FOR_LOGIN = new CountDownLatch(1);

    private static final AtomicBoolean LOGIN = new AtomicBoolean(false);
    private static final AtomicBoolean EXIT = new AtomicBoolean(false);

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
            try {
                //等收到服务端消息，就是channelRead那里会处理，否则一直阻塞，不进下面的处理
                WAIT_FOR_LOGIN.await();
            } catch (InterruptedException e) {
                log.error("{}异常", Const.getCurrentMethodMessage(this.getClass()), e);
            }
            //channelRead处理时，登录不成功则关闭通道
            if (!LOGIN.get()) {
                ctx.channel().close();
                return;
            }
            //每次发送成功，则不断循环等待输入指令进行下次发送
            while (true) {
                System.out.println("==================================");
                System.out.println("send [username] [content]");
                System.out.println("gsend [group name] [content]");
                System.out.println("gcreate [group name] [m1,m2,m3...]");
                System.out.println("gmembers [group name]");
                System.out.println("gjoin [group name]");
                System.out.println("gquit [group name]");
                System.out.println("quit");
                System.out.println("==================================");
                String command = null;
                try {
                    command = scanner.nextLine();
                } catch (Exception e) {
                    break;
                }
                if (EXIT.get()) {
                    return;
                }
                String[] s = command.split(" ");
                //根据输入的指令不同，构造不同消息类型，进行发送，让服务端得知是那种消息，以便进行转发
                switch (s[0]) {
                    case "send":
                        ctx.writeAndFlush(new ChatRequestMessage(username, s[1], s[2]));
                        break;
                    case "gsend":
                        ctx.writeAndFlush(new GroupChatRequestMessage(username, s[1], s[2]));
                        break;
                    case "gcreate":
                        Set<String> set = CollUtil.newHashSet(s[2].split(","));
                        set.add(username); // 加入自己
                        ctx.writeAndFlush(new GroupCreateRequestMessage(s[1], set));
                        break;
                    case "gmembers":
                        ctx.writeAndFlush(new GroupMembersRequestMessage(s[1]));
                        break;
                    case "gjoin":
                        ctx.writeAndFlush(new GroupJoinRequestMessage(username, s[1]));
                        break;
                    case "gquit":
                        ctx.writeAndFlush(new GroupQuitRequestMessage(username, s[1]));
                        break;
                    case "quit":
                        ctx.channel().close();
                        return;
                }
            }
        });
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("{} 参数msg: {}", Const.getCurrentMethodMessage(this.getClass()), msg);
        if (msg instanceof LoginResponseMessage) {
            LoginResponseMessage message = (LoginResponseMessage) msg;
            if (message.isSuccess()) {
                LOGIN.set(true);
            }
            WAIT_FOR_LOGIN.countDown();
        }
    }
}
