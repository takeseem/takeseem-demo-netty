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
package com.takeseem.demo.netty.chapter2;

import com.takeseem.demo.netty.UtilDemoNetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 代码清单 2-2 EchoServer 类
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class EchoServer {
	private int port;
	
	public EchoServer(int port) {
		this.port = port;
	}
	
	private void start() throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap boot = new ServerBootstrap();
			boot.group(group).channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(new EchoServerHandler());
					}
				});
			ChannelFuture bindFuture = boot.bind(port);
			bindFuture.addListener(f -> {
				if (f.isSuccess()) System.out.println("LISTEN " + bindFuture.channel().localAddress());
			});
			bindFuture.sync();
			
			bindFuture.channel().closeFuture().sync().addListener(f -> System.out.println("Server closed."));
		} finally {
			UtilDemoNetty.shutdownGracefullySync(group);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		int port = 40001;
		if (args.length > 0) port = Integer.parseInt(args[0]);
		
		new EchoServer(port).start();
	}
}
