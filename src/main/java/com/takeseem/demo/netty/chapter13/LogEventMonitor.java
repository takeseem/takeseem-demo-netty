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
package com.takeseem.demo.netty.chapter13;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;

/**
 * 代码清单 13-8 LogEventMonitor
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class LogEventMonitor {
	private final EventLoopGroup group;
	private final Bootstrap boot;
	
	public LogEventMonitor(InetSocketAddress address) {
		group = new EpollEventLoopGroup();
		boot = new Bootstrap();
		boot.group(group).channel(EpollDatagramChannel.class)
			.option(ChannelOption.SO_BROADCAST, true)
			.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(
							new LogEventDecoder(),
							new LogEventHandler()
							);
				}
			}).localAddress(address);
	}
	
	private void destroy() {
		group.shutdownGracefully();
	}
	private void run() throws InterruptedException {
		boot.bind().syncUninterruptibly().channel().closeFuture().sync();
	}
	
	public static void main(String[] args) throws InterruptedException {
		int port = 40001;
		if (args.length > 0) port = Integer.parseInt(args[0]);
		
		LogEventMonitor monitor = new LogEventMonitor(new InetSocketAddress(port));
		
		try {
			monitor.run();
		} finally {
			monitor.destroy();
		}
	}
}
