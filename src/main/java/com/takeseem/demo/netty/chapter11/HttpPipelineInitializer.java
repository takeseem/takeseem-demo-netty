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

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * 代码清单 11-2 添加 HTTP 支持
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class HttpPipelineInitializer extends ChannelInitializer<Channel> {
	private boolean client;

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		if (client) {
			pipeline.addLast(new HttpResponseDecoder());
			pipeline.addLast(new HttpRequestEncoder());
		} else {
			pipeline.addLast(new HttpRequestDecoder());
			pipeline.addLast(new HttpResponseEncoder());
		}
	}

	@Test
	public void test() {
		HttpPipelineInitializer clientInit = new HttpPipelineInitializer();
		clientInit.client = true;
		
		EmbeddedChannel serverChannel = new EmbeddedChannel(this), clientChannel = new EmbeddedChannel(clientInit);
		
		HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "http://www.baidu.com/");
		Assert.assertTrue(clientChannel.writeOutbound(request));
		serverChannel.writeInbound((Object) clientChannel.readOutbound());
		Assert.assertEquals(serverChannel.readInbound(), request);
		
		HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
				Unpooled.copiedBuffer("Hello.", CharsetUtil.UTF_8));
		Assert.assertTrue(serverChannel.writeOutbound(response));
		clientChannel.writeInbound((Object) serverChannel.readOutbound());
		Assert.assertEquals(clientChannel.readInbound(), response);
	}
}
