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
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.takeseem.demo.netty.UtilDemoNetty;

import io.netty.util.CharsetUtil;

/**
 * 代码清单 4-1 未使用 Netty 的异步网络编程：客户端进来后，使用线程处理，输出Hi后关闭连接
 * @author <a href="https://github.com/takeseem/">杨浩</a>
 */
public class PlainNioServer {

	public static void main(String[] args) throws IOException {
		int port = 40001;
		if (args.length > 0) port = Integer.parseInt(args[0]);
		
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("LISTEN :" + port);
			for (;;) {
				Socket socket = serverSocket.accept();
				System.out.println("socket: " + socket);
				new Thread(() -> {
					try (OutputStream out = socket.getOutputStream()) {
						out.write("Hi.\r\n".getBytes(CharsetUtil.UTF_8));
						out.flush();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						UtilDemoNetty.close(socket);
					}
				}, "socket-" + socket).start();
			}
		}
	}
}
