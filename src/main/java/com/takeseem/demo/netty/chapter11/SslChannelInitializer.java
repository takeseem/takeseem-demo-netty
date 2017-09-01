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
package com.takeseem.demo.netty.chapter11;

import javax.net.ssl.SSLEngine;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

/**
 * 代码清单 11-1 添加 SSL/TLS 支持
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {
	private final SslContext context;
	private final boolean startTls;
	
	public SslChannelInitializer(SslContext context, boolean startTls) {
		this.context = context;
		this.startTls = startTls;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		SSLEngine engine = context.newEngine(ch.alloc());
		ch.pipeline().addFirst("ssl", new SslHandler(engine, startTls));
	}
	
}
