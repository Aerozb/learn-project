package com.yhy.demo02.stickypackage;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class Clietnt {
    public static void main(String[] args) {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ChannelFuture channelFuture = new Bootstrap().
                    group(worker).
                    channel(NioSocketChannel.class).
                    handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                                      @Override
                                                      public void channelActive(ChannelHandlerContext ctx) {
                                                          log.debug("sending...");
                                                          ByteBuf buffer = ctx.alloc().buffer();
                                                          for (int i = 0; i < 10; i++) {
                                                              buffer.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
                                                          }
                                                          ctx.writeAndFlush(buffer);
                                                      }
                                                  }
                            );
                        }
                    })
                    .connect(new InetSocketAddress("localHost", 8080));
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("client error", e);
        } finally {
            worker.shutdownGracefully();
        }
    }
}
