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
package com.takeseem.demo.netty.chapter4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * 代码清单 4-4 使用 Netty 的异步网络处理: Epoll
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class NettyEpollServer {
	private int port;
	
	public NettyEpollServer(int port) {
		this.port = port;
	}
	
	private void start() throws InterruptedException {
		ByteBuf msg = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi.\r\n".getBytes(CharsetUtil.UTF_8)));
		EventLoopGroup group = new EpollEventLoopGroup();
		try {
			ServerBootstrap boot = new ServerBootstrap();
			boot.group(group)
				.channel(EpollServerSocketChannel.class)
				.childHandler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
							@Override
							public void channelActive(ChannelHandlerContext ctx) throws Exception {
								ctx.writeAndFlush(msg.duplicate()).addListener(ChannelFutureListener.CLOSE);
							}
						});
					}
				});
			ChannelFuture future = boot.bind(port).sync();
			future.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		int port = 40001;
		if (args.length > 0) port = Integer.parseInt(args[0]);
		
		new NettyEpollServer(port).start();
	}
}
