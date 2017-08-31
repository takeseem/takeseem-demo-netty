/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.takeseem.demo.netty.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.ResourceLeakDetector.Level;
import io.netty.util.concurrent.Future;

/**
 * 代码清单 8-5 引导服务器
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class SharingEventLoopGroup {

	public static void main(String[] args) throws InterruptedException {
		ResourceLeakDetector.setLevel(Level.PARANOID);
		int port = 40001;
		if (args.length > 0) port = Integer.parseInt(args[0]);
		
		EventLoopGroup parentGroup = new NioEventLoopGroup(), childGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap boot = new ServerBootstrap();
			boot.group(parentGroup, childGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
					private ChannelFuture connectFuture;
					private Channel remoteChannel;
					private CompositeByteBuf bufs;
					
					private void flushBufs() {
						remoteChannel.writeAndFlush(bufs);
					}
					
					@Override
					public void channelActive(ChannelHandlerContext ctx) throws Exception {
						Bootstrap remoteBoot = new Bootstrap();
						remoteBoot.group(ctx.channel().eventLoop()).channel(NioSocketChannel.class).handler(new SimpleChannelInboundHandler<ByteBuf>() {
							@Override
							protected void channelRead0(ChannelHandlerContext remoteCtx, ByteBuf msg) throws Exception {
								msg.retain();
								ctx.writeAndFlush(msg);
							}
						});
						connectFuture = remoteBoot.connect("www.manning.com", 80);
						connectFuture.addListener(f -> {
							if (f.isSuccess()) {
								System.out.println("connect www.manning.com ok");
								remoteChannel = connectFuture.channel();
								flushBufs();
							} else {
								System.out.println("connect www.manning.com error" + f.cause());
								ctx.close();
							}
						});
						bufs = ctx.alloc().compositeBuffer();
					}
					
					@Override
					protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
						msg.retain();
						bufs.addComponent(true, msg);
						if (remoteChannel != null) flushBufs();
					}
				});
	
			boot.bind(port).sync()
				.channel().closeFuture().sync();
		} finally {
			Future<?> future1 = childGroup.shutdownGracefully();
			Future<?> future2 = parentGroup.shutdownGracefully();
			future1.sync();
			future2.sync();
		}
	}
}
