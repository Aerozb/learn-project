package com.yhy.demo01.helloserver;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class Clietnt {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new Bootstrap().group(new NioEventLoopGroup()).channel(NioSocketChannel.class).handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new StringEncoder());
            }
        }).connect(new InetSocketAddress("localHost", 8080));
        //同步：连接成功后开始做事情
        //ChannelFuture sync = channelFuture.sync();
        //Channel channel = sync.channel();
        //channel.writeAndFlush("牛逼");

        //异步：连接成功后开始做事情
        channelFuture.addListener((ChannelFutureListener) future -> {
            ChannelFuture sync = channelFuture.sync();
            Channel channel = sync.channel();
            channel.writeAndFlush("牛逼");
        });
    }
}
