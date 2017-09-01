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

import org.junit.Assert;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * 代码清单 11-4 自动压缩 HTTP 消息
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class HttpCompressionInitializer extends ChannelInitializer<Channel> {
	private boolean client;

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		if (client) {
			pipeline.addLast("codec", new HttpClientCodec());
			pipeline.addLast("decompressor", new HttpContentDecompressor());
		} else {
			pipeline.addLast("codec", new HttpServerCodec());
			pipeline.addLast("compressor", new HttpContentCompressor());
		}
		pipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
	}

	@Test
	public void test() {
		HttpCompressionInitializer clientInit = new HttpCompressionInitializer();
		clientInit.client = true;
		
		EmbeddedChannel serverChannel = new EmbeddedChannel(this), clientChannel = new EmbeddedChannel(clientInit);
		
		//client request -> server
		HttpRequest requestInClient = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.baidu.com/");
		requestInClient.headers().add(HttpHeaderNames.ACCEPT_ENCODING, "gzip");
		Assert.assertTrue(clientChannel.writeOutbound(requestInClient));
		for (ByteBuf payload; (payload = clientChannel.readOutbound()) != null; ) {
			System.out.println("client -> server: " + payload.toString(CharsetUtil.UTF_8));
			serverChannel.writeInbound(payload);
		}
	
		//server response -> client
		FullHttpRequest requestInServer = serverChannel.readInbound();
		DefaultFullHttpResponse responseInServer = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.copiedBuffer(("Your request uri = " + requestInServer.uri()).getBytes(CharsetUtil.UTF_8)));
		Assert.assertTrue(serverChannel.writeOutbound(responseInServer));
		for (ByteBuf payload; (payload = serverChannel.readOutbound()) != null; ) {
			System.out.println("server -> client: " + payload.toString(CharsetUtil.UTF_8));
			clientChannel.writeInbound(payload);
		}
		
		FullHttpResponse responseInClient = clientChannel.readInbound();
		System.out.println("client response: " + responseInClient);
		String text = responseInClient.content().toString(CharsetUtil.UTF_8);
		System.out.println("client response content: " + text);
		Assert.assertTrue(text.startsWith("Your request uri = "));
	}
}
