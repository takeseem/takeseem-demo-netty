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

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * 代码清单 12-7 向 ChatServer 添加加密
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class SecureChatServer extends ChatServer {
	private final SslContext context;

	public SecureChatServer(int port, SslContext context) {
		super(port);
		this.context = context;
	}
	
	@Override
	protected ChannelInitializer<Channel> createInitializer(ChannelGroup channelGroup) {
		return new SecureChatServerInitializer(channelGroup, context);
	}

	public static void main(String[] args) throws Exception {
		int port = 40002;
		if (args.length > 0) port = Integer.parseInt(args[0]);
		
		SelfSignedCertificate cert = new SelfSignedCertificate();
		SslContext context = SslContextBuilder.forServer(cert.certificate(), cert.privateKey()).build();
		SecureChatServer server = new SecureChatServer(port, context);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			server.destroy();
		}, "main-shutdown"));
		server.start();
		server.waitChannelClose();
	}
}
