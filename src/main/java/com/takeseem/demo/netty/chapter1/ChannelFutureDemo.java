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
package com.takeseem.demo.netty.chapter1;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;

/**
 * 代码清单 1-3 异步地建立连接 & 代码清单 1-4 回调实战：一切异步执行
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class ChannelFutureDemo {

	public static void main(String[] args) {
		Channel channel = new EmbeddedChannel();	//EmbeddedChannel用来方便测试的
		ChannelFuture future = channel.connect(new InetSocketAddress("127.0.0.1", 40001));
		future.addListener(f -> {
			if (f.isSuccess()) {
				System.out.println("connect to " + channel.remoteAddress() + " successed.");
				ByteBuf buf = Unpooled.buffer();
				buf.writeBytes("Hello :)".getBytes(CharsetUtil.UTF_8));
				
				ChannelFuture writeFuture = channel.writeAndFlush(buf);
				writeFuture.addListener(wf -> {
					if (writeFuture.isSuccess()) {
						System.out.println("write ok.");
					} else {
						wf.cause().printStackTrace();
					}
				});
			} else {
				f.cause().printStackTrace();
			}
		});
	}
}
