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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * 代码清单 8-4 引导服务器
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class ServerBootstrapDemo {

	public static void main(String[] args) throws InterruptedException {
		int port = 40001;
		if (args.length > 0) port = Integer.parseInt(args[0]);
		
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap boot = new ServerBootstrap();
			boot.group(group).channel(NioServerSocketChannel.class).childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
				@Override
				protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
					System.out.println("received data:");
					System.out.println(msg.toString(CharsetUtil.UTF_8));
				}
			});
	
			boot.bind(port)
				.addListener(f -> {
					if (f.isSuccess()) {
						System.out.println("bind success");
					} else {
						System.out.println("connect error: " + f.cause());
					}
				}).sync()
				.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
}
