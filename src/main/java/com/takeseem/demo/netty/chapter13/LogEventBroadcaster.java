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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollDatagramChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;

/**
 * 代码清单 13-3 LogEventBroadcaster
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class LogEventBroadcaster {
	private final EventLoopGroup group;
	private final Bootstrap bootstrap;
	private final File file;
	
	public LogEventBroadcaster(InetSocketAddress address, File file) {
		group = new EpollEventLoopGroup();
		bootstrap = new Bootstrap();
		bootstrap.group(group)
			.channel(EpollDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true)
			.handler(new LogEventEncoder(address));
		this.file = file;
	}
	
	public void run() throws InterruptedException, IOException {
		Channel ch = bootstrap.bind(0).sync().channel();
		long pointer = 0;
		for (;;) {
			long len = file.length();
			if (len < pointer) {
				pointer = len;
			} else if (len > pointer) {
				try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
					raf.seek(pointer);
					for (String line; (line = raf.readLine()) != null;) {
						ch.writeAndFlush(new LogEvent(null, -1, file.getAbsolutePath(), line));
					}
					pointer = raf.getFilePointer();
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				Thread.interrupted();
				break;
			}
		}
	}
	
	public void destroy() {
		group.shutdownGracefully();
	}
	
	public static void main(String[] args) throws InterruptedException, IOException {
		String file = "/tmp/LogEventBroadcaster.log";
		int port = 40001;
		if (args.length > 0) port = Integer.parseInt(args[0]);
		if (args.length > 1) file = args[1];
		LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255", port), new File(file));
		
		try {
			broadcaster.run();
		} finally {
			broadcaster.destroy();
		}
	}
}
