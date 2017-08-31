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

import org.junit.Assert;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;

/**
 * 代码清单 9-4 测试AbsIntegerEncoder
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class AbsIntegerEncoderTest {

	@Test
	public void test() {
		ByteBuf buf = Unpooled.buffer();
		for (int i = 1; i < 10; i++) {
			buf.writeInt(i * -1);
		}
		
		EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
		Assert.assertTrue(channel.writeOutbound(buf));
		Assert.assertTrue(channel.finish());
		
		for (int i = 1; i < 10; i++) {
			Assert.assertEquals(channel.<Object>readOutbound(), i);
		}
		Assert.assertNull(channel.readOutbound());
	}

}
