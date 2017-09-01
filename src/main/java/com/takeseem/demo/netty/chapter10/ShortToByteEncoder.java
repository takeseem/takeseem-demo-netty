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

import org.junit.Assert;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 代码清单 10-5 ShortToByteEncoder 类
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class ShortToByteEncoder extends MessageToByteEncoder<Short> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Short msg, ByteBuf out) throws Exception {
		out.writeShort(msg);
	}

	@Test
	public void test() {
		EmbeddedChannel channel = new EmbeddedChannel(this);
		
		short val = 1;
		channel.writeOutbound(val);
		Assert.assertEquals(channel.<ByteBuf>readOutbound().readShort(), val);
	}
}
