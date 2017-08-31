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
package com.takeseem.demo.netty.chapter6;

import org.junit.Assert;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * 代码清单 6-1 释放消息资源
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
@Sharable
public class DiscardHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ReferenceCountUtil.release(msg);	//已经释放，相当于消费掉，所以不要执行后续hander
	}
	
	@Test
	public void test() {
		ByteBuf msg = Unpooled.copiedBuffer("哈哈".getBytes());
		EmbeddedChannel channel = new EmbeddedChannel(this);
		
		Assert.assertTrue(msg.refCnt() == 1);
		channel.writeInbound(msg);
		Assert.assertTrue(msg.refCnt() == 0);
	}
}
