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
package com.takeseem.demo.netty.chapter9;

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
 * 代码清单 9-5 FrameChunkDecoder
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class FrameChunkDecoder extends ByteToMessageDecoder {
	private final int maxFrameSize;

	public FrameChunkDecoder() {
		maxFrameSize = 3;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int readableBytes = in.readableBytes();
		if (readableBytes > maxFrameSize) {
			in.clear();
			throw new TooLongFrameException("maxFrameSize=" + maxFrameSize);
		}
		
		out.add(in.readBytes(readableBytes));
	}

	/** 代码清单 9-6 测试FrameChunkDecoder */
	@Test
	public void test() {
		ByteBuf buf = Unpooled.buffer();
		for (int i = 0; i < 9; i++) {
			buf.writeByte(i);
		}
		
		ByteBuf input = buf.duplicate();
		EmbeddedChannel channel = new EmbeddedChannel(this);
		Assert.assertTrue(channel.writeInbound(input.readBytes(2)));
		
		try {
			channel.writeInbound(input.readBytes(4));
			Assert.fail();
		} catch (TooLongFrameException e) {
		}
		
		Assert.assertTrue(channel.writeInbound(input.readBytes(3)));
		channel.finish();
		
		ByteBuf read = channel.readInbound();
		Assert.assertEquals(read, buf.readSlice(2));
		read.release();
		
		read = channel.readInbound();
		Assert.assertEquals(read, buf.skipBytes(4).readSlice(3));
		read.release();
		
		buf.release();
	}
}
