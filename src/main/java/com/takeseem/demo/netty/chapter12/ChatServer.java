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
package com.takeseem.demo.netty.chapter12;

import java.util.ArrayList;
import java.util.List;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.ImmediateEventExecutor;

/**
 * 代码清单 12-4 引导服务器
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class ChatServer {
	private int port;
	private EventLoopGroup parentGroup, childGroup;
	private ChannelGroup channelGroup;
	private ChannelFuture channelFuture;

	public ChatServer(int port) {
		this.port = port;
	}
	
	public void start() {
		ServerBootstrap boot = new ServerBootstrap();
		parentGroup = new NioEventLoopGroup();
		childGroup = new NioEventLoopGroup();
		channelGroup = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
		boot.group(parentGroup, childGroup).channel(NioServerSocketChannel.class).childHandler(new ChatServerInitializer(channelGroup));
		
		channelFuture = boot.bind(port).syncUninterruptibly();
		System.out.println("ChatServer LISTEN " + port);
	}
	
	public void destroy() {
		System.out.println("ChatServer destroy...");
		List<Future<?>> futures = new ArrayList<>();
		if (channelFuture != null) futures.add(channelFuture.channel().close()); 
		if (channelGroup != null) futures.add(channelGroup.close());
		if (childGroup != null) futures.add(childGroup.shutdownGracefully());
		if (parentGroup != null) futures.add(parentGroup.shutdownGracefully());
		for (Future<?> future : futures) {
			future.syncUninterruptibly();
		}
		System.out.println("ChatServer destroy done.");
	}
	
	public void waitChannelClose() {
		if (channelFuture != null) channelFuture.channel().closeFuture().syncUninterruptibly();
	}
	
	public static void main(String[] args) {
		int port = 40001;
		if (args.length > 0) port = Integer.parseInt(args[0]);
		
		ChatServer server = new ChatServer(port);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			server.destroy();
		}, "main-shutdown"));
		server.start();
		server.waitChannelClose();
	}
}
