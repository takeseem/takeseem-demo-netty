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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

/**
 * 代码清单 10-4 TooLongFrameException
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class SafeByteToMessageDecoder extends ByteToMessageDecoder {
	private static final int maxFrameSize = 1024;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int readableBytes = in.readableBytes();
		if (readableBytes > maxFrameSize) {
			in.skipBytes(readableBytes);
			throw new TooLongFrameException("maxFrameSize=" + maxFrameSize);
		}
	}

	@Test
	public void test() {
		EmbeddedChannel channel = new EmbeddedChannel(this);
		channel.writeInbound(Unpooled.buffer().writeBytes(new byte[1024]));
		
		try {
			channel.writeInbound(Unpooled.buffer().writeBytes(new byte[1025]));
			Assert.fail();
		} catch (TooLongFrameException e) {}
	}
}
