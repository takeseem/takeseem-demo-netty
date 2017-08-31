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

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * 代码清单 8-1 引导一个客户端
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class BootstrapDemo {

	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap boot = new Bootstrap();
			boot.group(group).channel(NioSocketChannel.class).handler(new SimpleChannelInboundHandler<ByteBuf>() {
				@Override
				protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
					System.out.println("received data:");
					System.out.println(msg.toString(CharsetUtil.UTF_8));
				}
			});
	
			boot.connect(new InetSocketAddress("www.manning.com", 80)).addListener(f -> {
				if (f.isSuccess()) {
					System.out.println("connect success");
				} else {
					System.out.println("connect error: " + f.cause());
				}
			}).sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
}
