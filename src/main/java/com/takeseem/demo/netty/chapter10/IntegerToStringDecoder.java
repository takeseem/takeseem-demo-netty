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

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * 代码清单 10-3 IntegerToStringDecoder 类
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {

	@Override
	protected void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
		out.add(String.valueOf(msg));
	}
	
	@Test
	public void test() {
		EmbeddedChannel channel = new EmbeddedChannel(new ToIntegerDecoder(), this);
		Assert.assertTrue(channel.writeInbound(Unpooled.buffer().writeBytes(new byte[] {0, 0, 0, 1})));
		Assert.assertEquals(channel.readInbound(), "1");
		
		Assert.assertFalse(channel.writeInbound(Unpooled.buffer().writeBytes(new byte[] {0, 0, 0})));
		Assert.assertNull(channel.readInbound());
		
		Assert.assertTrue(channel.writeInbound(Unpooled.buffer().writeByte(1)));
		Assert.assertEquals(channel.readInbound(), "1");
	}
}
