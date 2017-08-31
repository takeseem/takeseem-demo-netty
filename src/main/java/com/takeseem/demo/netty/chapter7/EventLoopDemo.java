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
package com.takeseem.demo.netty.chapter7;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class EventLoopDemo {
	EmbeddedChannel channel = new EmbeddedChannel();
	
	/** 代码清单 7-3 使用 EventLoop 调度任务 */
	@Test
	public void schedule() throws InterruptedException, ExecutionException {
		long now = System.currentTimeMillis();
		channel.eventLoop()
			.schedule(() -> System.out.println("after: " + (System.currentTimeMillis() - now) + "ms"), 100, TimeUnit.MILLISECONDS);
		Thread.sleep(100);
		channel.write(Unpooled.EMPTY_BUFFER);	//仅仅触发事件循环
	}
	
	/** 代码清单 7-4 使用 EventLoop 调度周期性的任务 */
	@Test
	public void scheduleAtFixedRate() throws InterruptedException, ExecutionException {
		ScheduledFuture<?> future = channel.eventLoop()
			.scheduleAtFixedRate(new Runnable() {
				long last = System.currentTimeMillis();
				@Override
				public void run() {
					long now = System.currentTimeMillis();
					System.out.println("scheduleAtFixedRate after: " + (now - last) + "ms");
					last = now;
				}
			}, 100, 100, TimeUnit.MILLISECONDS);
		for (int i = 0; i < 4; i++) {
			Thread.sleep(100);
			channel.write(Unpooled.EMPTY_BUFFER);
		}
		
		//代码清单 7-5 使用 ScheduledFuture 取消任务
		System.out.println("cancel: " + future.cancel(false));
		for (int i = 0; i < 2; i++) {
			Thread.sleep(100);
			channel.write(Unpooled.EMPTY_BUFFER);
		}
	}
}
