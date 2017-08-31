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
package com.takeseem.demo.netty.chapter5;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 * 第 5 章 ByteBuf
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class ByteBufDemo {
	
	/** 代码清单 5-1 支撑数组 */
	@Test
	public void heapByteBuf() {
		String msg = "a heap ByteBuf";
		ByteBuf buf = Unpooled.buffer(1024);
		buf.writeBytes(msg.getBytes());
		if (buf.hasArray()) {
			byte[] array = buf.array();
			int offset = buf.arrayOffset() + buf.readerIndex();
			int len = buf.readableBytes();
			Assert.assertEquals(new String(array, offset, len), msg);
		} else {
			Assert.fail();
		}
	}
	
	/** 代码清单 5-2 访问直接缓冲区的数据 */
	@Test
	public void directBuffer() {
		String msg = "a direct ByteBuf.";
		ByteBuf buf = Unpooled.directBuffer(1024);
		buf.writeBytes(msg.getBytes());
		if (buf.hasArray()) {
			Assert.fail();
		} else {
			int len = buf.readableBytes();
			byte[] array = new byte[len];
			buf.getBytes(buf.readerIndex(), array);
			Assert.assertEquals(new String(array), msg);
		}
	}
	
	
	String headerTxt = "header is here.", bodyTxt = "body is here.";
	/** 代码清单 5-3 使用 ByteBuffer 的复合缓冲区模式 */
	@Test
	public void compositeByteBuffer() {
		ByteBuffer header = ByteBuffer.wrap(headerTxt.getBytes()), body = ByteBuffer.wrap(bodyTxt.getBytes());
		
		ByteBuffer msg = ByteBuffer.allocate(header.remaining() + body.remaining());
		msg.put(header);
		msg.put(body);
		msg.flip();
		
		byte[] dst = new byte[msg.remaining()];
		msg.get(dst);
		Assert.assertEquals(new String(dst), headerTxt + bodyTxt);
	}
	/** 代码清单 5-4 使用 CompositeByteBuf 的复合缓冲区模式 */
	@Test
	public void compositeByteBuf() {
		ByteBuf header = Unpooled.copiedBuffer(headerTxt.getBytes()), body = Unpooled.copiedBuffer(bodyTxt.getBytes());
		CompositeByteBuf msg = Unpooled.compositeBuffer();
		msg.addComponent(true, header);
		Assert.assertTrue(msg.hasArray());
		
		msg.addComponent(true, body);
		Assert.assertFalse(msg.hasArray());
		
		Assert.assertEquals(toString(msg), headerTxt + bodyTxt);
		
		msg.removeComponent(0);
		msg.forEach(comp -> System.out.println(comp.toString()));
		Assert.assertEquals(msg.numComponents(), 1);
	}
	
	/** 代码清单 5-5 访问 CompositeByteBuf 中的数据 */
	private String toString(CompositeByteBuf msg) {
		int len = msg.readableBytes();
		byte[] array = new byte[len];
		msg.getBytes(msg.readerIndex(), array);
		return new String(array);
	}
	
	/** 代码清单 5-6 (随机)访问数据 */
	@Test
	public void getByte()  {
		ByteBuf msg = Unpooled.buffer(9);
		msg.writeBytes("abc".getBytes());
		System.out.println("capacity=" + msg.capacity());
		for (int i = 0; i < msg.capacity(); i++) {
			System.out.print((char) msg.getByte(i));
		}
		System.out.println("<==");
	}
	
	/** 代码清单 5-7 读取所有数据 */
	@Test
	public void readByte()  {
		ByteBuf msg = Unpooled.buffer(9);
		msg.writeBytes("abc".getBytes());
		System.out.println("capacity=" + msg.capacity());
		while (msg.isReadable()) {
			System.out.print((char) msg.readByte());
		}
		System.out.println("<==");
	}
	
	/** 代码清单 5-11 复制一个 ByteBuf */
	@Test
	public void copy() {
		ByteBuf msg = Unpooled.copiedBuffer("Copy is Copy.".getBytes(CharsetUtil.UTF_8));
		ByteBuf copy = msg.copy();
		copy.setByte(0, 'Y');
		Assert.assertNotEquals(msg.getByte(0), copy.getByte(0));
		System.out.println(msg.toString(CharsetUtil.UTF_8) + " != " + copy.toString(CharsetUtil.UTF_8));
		
		ByteBuf slice = msg.slice();
		slice.setByte(0, '^');
		Assert.assertEquals(msg.getByte(0), slice.getByte(0));
		System.out.println(msg.toString(CharsetUtil.UTF_8) + " == " + slice.toString(CharsetUtil.UTF_8));
	}
	
	/** 代码清单 5-12 get() 和 set() 方法的用法 */
	@Test
	public void getSet() {
		ByteBuf msg = Unpooled.copiedBuffer("Netty in action rocks.".getBytes(CharsetUtil.UTF_8));
		int readerIndex = msg.readerIndex(), writerIndex = msg.writerIndex();
		msg.getByte(0);
		msg.setByte(0, 'B');
		Assert.assertEquals(msg.getByte(0), 'B');
		Assert.assertEquals(msg.readerIndex(), readerIndex);
		Assert.assertEquals(msg.writerIndex(), writerIndex);
	}
	
	/** 代码清单 5-13 ByteBuf 上的 read() 和 write() 操作 */
	@Test
	public void readWrite() {
		ByteBuf msg = Unpooled.buffer(64);
		msg.writeBytes("Netty in action rocks.".getBytes(CharsetUtil.UTF_8));
		
		System.out.println((char) msg.readByte());
		int readerIndex = msg.readerIndex(), writerIndex = msg.writerIndex();
		msg.writeByte('E');
		Assert.assertEquals(msg.readerIndex(), readerIndex);
		Assert.assertNotEquals(msg.writerIndex(), writerIndex);
		System.out.println(msg.toString(CharsetUtil.UTF_8));
	}
	
	/** 代码清单 5-15 引用计数 & 代码清单 5-16 释放引用计数的对象 */
	@Test
	public void refCnt() {
		ByteBuf msg = Unpooled.buffer(64);
		Assert.assertEquals(msg.refCnt(), 1);
		
		msg.release();
		Assert.assertEquals(msg.refCnt(), 0);
	}
}
