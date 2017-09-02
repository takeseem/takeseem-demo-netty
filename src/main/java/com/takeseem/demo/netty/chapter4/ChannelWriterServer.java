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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * 代码清单 4-5 写出到 Channel & 代码清单 4-6 从多个线程使用同一个 Channel:<br>
 * 建立连接时：写Hello.<br>
 * 输入start时，开始多线程写，直到输入stop<br>
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class ChannelWriterServer {
	private int port;
	
	public ChannelWriterServer(int port) {
		this.port = port;
	}
	
	private void start() throws InterruptedException {
		EventLoopGroup group = new EpollEventLoopGroup();
		Executor executor = Executors.newCachedThreadPool();
		try {
			ServerBootstrap boot = new ServerBootstrap();
			boot.group(group)
				.channel(EpollServerSocketChannel.class)
				.childHandler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
							private List<RunWriter> runs = new ArrayList<>();
							
							@Override
							public void channelActive(ChannelHandlerContext ctx) throws Exception {
								System.out.println("client " + ctx.channel().remoteAddress() + " connected.");
								ctx.writeAndFlush(Unpooled.copiedBuffer("Hello.\r\n".getBytes(CharsetUtil.UTF_8))).addListener(f -> {
									if (f.isSuccess()) {
										System.out.println("Active write Hello success");
									} else {
										System.out.println("Active write Hello error");
										f.cause().printStackTrace();
									}
								});
								super.channelActive(ctx);
							}
							@Override
							public void channelInactive(ChannelHandlerContext ctx) throws Exception {
								System.out.println("client " + ctx.channel().remoteAddress() + " disconnected.");
								super.channelInactive(ctx);
							}
							
							@Override
							protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
								if (msg.readableBytes() > 0) {
									String line = msg.toString(CharsetUtil.UTF_8).trim();
									if ("start".equals(line)) {
										RunWriter run = new RunWriter(ch);
										runs.add(run);
										executor.execute(run);
										ctx.writeAndFlush(Unpooled.copiedBuffer(("start: " + run + "\r\n").getBytes(CharsetUtil.UTF_8)));
									} else if ("stop".equals(line)) {
										if (runs.isEmpty()) {
											ctx.writeAndFlush(Unpooled.copiedBuffer(("nothing to stop.\r\n").getBytes(CharsetUtil.UTF_8)));
										} else {
											for (Iterator<RunWriter> iter = runs.iterator(); iter.hasNext(); ) {
												RunWriter run = iter.next();
												iter.remove();
												run.stop();
												ctx.writeAndFlush(Unpooled.copiedBuffer(("stop: " + run + "\r\n").getBytes(CharsetUtil.UTF_8)));
												break;
											}
										}
									}
								}
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
	
	private static final class RunWriter implements Runnable {
		private volatile boolean stop;
		private Channel channel;
		
		public RunWriter(Channel channel) {
			this.channel = channel;
		}

		@Override
		public void run() {
			for (int i = 0; !stop && i < 10; i++) {
				channel.writeAndFlush(Unpooled.copiedBuffer(("Thread[" + Thread.currentThread().getName()  + "] i=" + i + "\r\n").getBytes(CharsetUtil.UTF_8)));
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.interrupted();
					break;
				}
			}
		}
		
		public void stop() {
			stop = true;
			channel.writeAndFlush(Unpooled.copiedBuffer(("Thread[" + Thread.currentThread().getName()  + "] stop.\r\n").getBytes(CharsetUtil.UTF_8)));
		}
		
	}
	public static void main(String[] args) throws InterruptedException {
		int port = 40001;
		if (args.length > 0) port = Integer.parseInt(args[0]);
		
		new ChannelWriterServer(port).start();
	}
}
