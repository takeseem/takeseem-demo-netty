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
package com.takeseem.demo.netty.chapter4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import io.netty.util.CharsetUtil;

/**
 * 代码清单 4-2 未使用 Netty 的异步网络编程：客户端进来后，使用线程处理，输出Hi后关闭连接
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class PlainOioServer {

	public static void main(String[] args) throws IOException {
		int port = 40001;
		if (args.length > 0) port = Integer.parseInt(args[0]);
		
		try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
			serverSocketChannel.configureBlocking(false);
			try (ServerSocket serverSocket = serverSocketChannel.socket()) {
				serverSocket.bind(new InetSocketAddress(port));
				try (Selector selector = Selector.open()) {
					serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
					ByteBuffer msg = ByteBuffer.wrap(("Hi.\r\n".getBytes(CharsetUtil.UTF_8)));
					System.out.println("LISTEN :" + port);
					for (;;) {
						try {
							selector.select();
						} catch (IOException e) {
							e.printStackTrace();
							break;
						}
						
						Set<SelectionKey> keys = selector.selectedKeys();
						for (Iterator<SelectionKey> iter = keys.iterator(); iter.hasNext();) {
							SelectionKey key = iter.next();
							iter.remove();
							
							if (key.isAcceptable()) {
								ServerSocketChannel server = (ServerSocketChannel) key.channel();	// server === serverSocketChannel
								SocketChannel channel = server.accept();
								channel.configureBlocking(false);
								channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, msg.duplicate());
								System.out.println("Accepted connection from " + channel);
							}
							
							if (key.isWritable()) {
								SocketChannel channel = (SocketChannel) key.channel();
								for (ByteBuffer buf = (ByteBuffer) key.attachment(); buf.hasRemaining(); ) {
									if (channel.write(buf) == 0) break;
								}
								channel.close();
							}
						}
					}
				}
			}
		}
	}
}
