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

import io.netty.buffer.Unpooled;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.channel.embedded.EmbeddedChannel;

/**
 * 代码清单 10-10 CombinedChannelDuplexHandler<I,O>
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {

	public CombinedByteCharCodec() {
		super(new ByteToCharDecoder(), new CharToByteEncoder());
	}
	
	@Test
	public void test() {
		EmbeddedChannel channel = new EmbeddedChannel(this);
		
		Character ch = '我';
		channel.writeInbound(ch);
		Assert.assertEquals(channel.readInbound(), ch);
		
		channel.writeOutbound(ch);
		Assert.assertEquals(channel.readOutbound(), Unpooled.buffer().writeChar(ch));
	}
}
