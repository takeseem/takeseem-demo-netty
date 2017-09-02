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

import java.io.File;
import java.io.RandomAccessFile;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

/**
 * 代码清单 12-1 HTTPRequestHandler
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class HTTPRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	private File index;
	private String wsUri;
	
	public HTTPRequestHandler(String wsUri) {
		index = new File(Thread.currentThread().getContextClassLoader().getResource("chapter2/index.html").getFile());
		this.wsUri = wsUri;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if (wsUri.equals(request.uri())) {	// websocket的请求
			ctx.fireChannelRead(request.retain());
		} else {
			if (HttpUtil.is100ContinueExpected(request)) {
				System.out.println("发送 100-continue ");
				ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE));
			}
			
			System.out.println("发送index页: " + index);
			@SuppressWarnings("resource")
			RandomAccessFile indexRAF = new RandomAccessFile(index, "r");
			HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
			response.headers().add(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");
			boolean keepAlive = HttpUtil.isKeepAlive(request); 
			if (keepAlive) {
				response.headers().add(HttpHeaderNames.CONTENT_LENGTH, indexRAF.length());
				response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			}
			
			ctx.write(response);
			if (ctx.pipeline().get(SslHandler.class) == null) {
				ctx.write(new DefaultFileRegion(index, 0, indexRAF.length()));
			} else {	//SSL是加密的不能用zero-copy，所以使用Chunked
				ctx.write(new ChunkedNioFile(indexRAF.getChannel()));
			}
			
			ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
			if (!keepAlive) future.addListener(ChannelFutureListener.CLOSE);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
