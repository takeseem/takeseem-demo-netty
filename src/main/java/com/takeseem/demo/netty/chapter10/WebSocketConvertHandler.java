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
package com.takeseem.demo.netty.chapter10;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.takeseem.demo.netty.chapter10.WebSocketConvertHandler.MyWebSocketFrame.Type;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * 代码清单 10-7 使用 MessageToMessageCodec
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class WebSocketConvertHandler extends MessageToMessageCodec<WebSocketFrame, WebSocketConvertHandler.MyWebSocketFrame> {
	
	@Override
	protected void encode(ChannelHandlerContext ctx, MyWebSocketFrame msg, List<Object> out) throws Exception {
		Type type = msg.getType();
		ByteBuf payload = msg.getData().duplicate().retain();
		if (type == Type.binary) {
			out.add(new BinaryWebSocketFrame(payload));
		} else if (type == Type.close) {
			out.add(new CloseWebSocketFrame(true, 0, payload));
		} else if (type == Type.ping) {
			out.add(new PingWebSocketFrame(payload));
		} else if (type == Type.pong) {
			out.add(new PongWebSocketFrame(payload));
		} else if (type == Type.text) {
			out.add(new TextWebSocketFrame(payload));
		} else if (type == Type.continuation) {
			out.add(new ContinuationWebSocketFrame(payload));
		} else {
			throw new IllegalStateException("error type: " + msg);
		}
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) throws Exception {
		Type type;
		if (msg instanceof BinaryWebSocketFrame) {
			type = Type.binary;
		} else if (msg instanceof CloseWebSocketFrame) {
			type = Type.close;
		} else if (msg instanceof PingWebSocketFrame) {
			type = Type.ping;
		} else if (msg instanceof PongWebSocketFrame) {
			type = Type.pong;
		} else if (msg instanceof TextWebSocketFrame) {
			type = Type.text;
		} else if (msg instanceof ContinuationWebSocketFrame) {
			type = Type.continuation;
		} else {
			throw new IllegalStateException("error frame: " + msg);
		}
		out.add(new MyWebSocketFrame(type, msg.content().retain()));
	}
	
	public static final class MyWebSocketFrame {
		public enum Type {
			binary, close, ping, pong, text, continuation
		}
		private Type type;
		private ByteBuf data;
		
		public MyWebSocketFrame(Type type, ByteBuf data) {
			this.type = type;
			this.data = data;
		}
		
		public Type getType() {
			return type;
		}
		public void setType(Type type) {
			this.type = type;
		}
		public ByteBuf getData() {
			return data;
		}
		public void setData(ByteBuf data) {
			this.data = data;
		}
	}

	@Test
	public void test() {
		EmbeddedChannel channel = new EmbeddedChannel(this);
		WebSocketFrame frame = new TextWebSocketFrame("hello text.");
		channel.writeInbound(frame);
		
		Object read = channel.readInbound();
		Assert.assertTrue(read instanceof MyWebSocketFrame);
		
		channel.writeOutbound(read);
		Assert.assertEquals(channel.readOutbound(), frame);
		channel.finish();
	}
}
