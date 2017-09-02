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
package com.takeseem.demo.netty.chapter13;

import java.net.InetSocketAddress;

/**
 * 代码清单 13-1 LogEvent 消息
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class LogEvent {
	public static final byte separator = ':';
	private InetSocketAddress source;
	private String logfile;
	private String msg;
	private long received;
	
	public LogEvent(InetSocketAddress source, long received, String logfile, String msg) {
		this.source = source;
		this.logfile = logfile;
		this.msg = msg;
		this.received = received;
	}
	
	public InetSocketAddress getSource() {
		return source;
	}
	public String getLogfile() {
		return logfile;
	}
	public String getMsg() {
		return msg;
	}
	public long getReceived() {
		return received;
	}
}
