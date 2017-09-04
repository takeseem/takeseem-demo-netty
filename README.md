## License
许可协议 [Apache License][].

[Apache License]: http://www.apache.org/licenses/LICENSE-2.0

## 项目

### 第 1 章 Netty ——异步和事件驱动
* [代码清单 1-1 阻塞 I/O 示例](src/main/java/com/takeseem/demo/netty/chapter1/ServerSocketDemo.java)
* [代码清单 1-2 被回调触发的 ChannelHandler](src/main/java/com/takeseem/demo/netty/chapter1/ChannelHandler.java)
* [代码清单 1-3 异步地建立连接 & 代码清单 1-4 回调实战](src/main/java/com/takeseem/demo/netty/chapter1/ChannelFutureDemo.java)

### 第 2 章 你的第一款 Netty 应用程序
* [代码清单 2-1 EchoServerHandler](src/main/java/com/takeseem/demo/netty/chapter2/EchoServerHandler.java)
* [代码清单 2-2 EchoServer 类](src/main/java/com/takeseem/demo/netty/chapter2/EchoServer.java)
* [代码清单 2-3 客户端的 EchoClientHandler 类](src/main/java/com/takeseem/demo/netty/chapter2/EchoClientHandler.java)
* [代码清单 2-4 客户端的主类 EchoClient](src/main/java/com/takeseem/demo/netty/chapter2/EchoClient.java)

### 第 4 章 传输
* [代码清单 4-1 未使用 Netty 的阻塞网络编程](src/main/java/com/takeseem/demo/netty/chapter4/PlainOioServer.java)
* [代码清单 4-2 未使用 Netty 的异步网络编程](src/main/java/com/takeseem/demo/netty/chapter4/PlainNioServer.java)
* [代码清单 4-3 使用 Netty 的阻塞网络处理](src/main/java/com/takeseem/demo/netty/chapter4/NettyOioServer.java)
* [代码清单 4-4 使用 Netty 的异步网络处理](src/main/java/com/takeseem/demo/netty/chapter4/NettyNioServer.java)
* [Netty Epoll](src/main/java/com/takeseem/demo/netty/chapter4/NettyEpollServer.java)
* [代码清单 4-5 写出到 Channel & 代码清单 4-6 从多个线程使用同一个 Channel](src/main/java/com/takeseem/demo/netty/chapter4/ChannelWriterServer.java)

### 第 5 章 ByteBuf
* [ByteBuf示例代码](src/main/java/com/takeseem/demo/netty/chapter5/ByteBufDemo.java)
  * Unpooled.copiedBuffer 最大容量是初始长度，所以write时可能越界
 
### 第 6 章 ChannelHandler和ChannelPipeline
* [代码清单 6-1 释放消息资源](src/main/java/com/takeseem/demo/netty/chapter6/DiscardHandler.java)
* [代码清单 6-2 使用 SimpleChannelInboundHandler](src/main/java/com/takeseem/demo/netty/chapter6/SimpleDiscardHandler.java)

### 第 7 章 EventLoop和线程模型
* [代码清单 7-3 使用 EventLoop 调度任务](src/main/java/com/takeseem/demo/netty/chapter7/EventLoopDemo.java)

### 第 8 章 引导
* [代码清单 8-1 引导一个客户端](src/main/java/com/takeseem/demo/netty/chapter8/BootstrapDemo.java)
* [代码清单 8-4 引导服务器](src/main/java/com/takeseem/demo/netty/chapter8/ServerBootstrapDemo.java)
* [代码清单 8-5 引导服务器 (共享EventLoop)](src/main/java/com/takeseem/demo/netty/chapter8/SharingEventLoopGroup.java)
  * 共享EventLoop，原基础只连接，无法获取页面，利用前面所学现在能正常访问某个网页
 
### 第 9 章 单元测试
* [代码清单 9-2 测试 FixedLengthFrameDecoder](src/main/java/com/takeseem/demo/netty/chapter9/FixedLengthFrameDecoderTest.java)
* [代码清单 9-3 AbsIntegerEncoder](src/main/java/com/takeseem/demo/netty/chapter9/AbsIntegerEncoder.java)
* [代码清单 9-4 测试AbsIntegerEncoder](src/main/java/com/takeseem/demo/netty/chapter9/AbsIntegerEncoderTest.java)
* [代码清单 9-5 FrameChunkDecoder & 代码清单 9-6 测试FrameChunkDecoder](src/main/java/com/takeseem/demo/netty/chapter9/FrameChunkDecoder.java)

### 第 10 章 编解码器框架
* [代码清单 10-1 ToIntegerDecoder 类扩展了 ByteToMessageDecoder](src/main/java/com/takeseem/demo/netty/chapter10/ToIntegerDecoder.java)
* [代码清单 10-2 ToIntegerDecoder2 类扩展了 ReplayingDecoder](src/main/java/com/takeseem/demo/netty/chapter10/ToIntegerDecoder2.java)
* [代码清单 10-3 IntegerToStringDecoder 类](src/main/java/com/takeseem/demo/netty/chapter10/IntegerToStringDecoder.java)
* [代码清单 10-4 TooLongFrameException](src/main/java/com/takeseem/demo/netty/chapter10/SafeByteToMessageDecoder.java)
* [代码清单 10-5 ShortToByteEncoder 类](src/main/java/com/takeseem/demo/netty/chapter10/ShortToByteEncoder.java)
* [代码清单 10-6 IntegerToStringEncoder 类](src/main/java/com/takeseem/demo/netty/chapter10/IntegerToStringEncoder.java)
* [代码清单 10-7 使用 MessageToMessageCodec](src/main/java/com/takeseem/demo/netty/chapter10/WebSocketConvertHandler.java)
* [代码清单 10-8 ByteToCharDecoder 类](src/main/java/com/takeseem/demo/netty/chapter10/ByteToCharDecoder.java)
* [代码清单 10-9 CharToByteEncoder 类](src/main/java/com/takeseem/demo/netty/chapter10/CharToByteEncoder.java)
* [代码清单 10-10 CombinedChannelDuplexHandler<I,O>](src/main/java/com/takeseem/demo/netty/chapter10/CombinedByteCharCodec.java)

### 第 11 章 预制的ChannelHandler和编解码器
* [代码清单 11-1 添加 SSL/TLS 支持](src/main/java/com/takeseem/demo/netty/chapter11/SslChannelInitializer.java)
* [代码清单 11-2 添加 HTTP 支持](src/main/java/com/takeseem/demo/netty/chapter11/.java)
* [代码清单 11-3 自动聚合 HTTP 的消息片段](src/main/java/com/takeseem/demo/netty/chapter11/HttpAggregatorInitializer.java)
* [代码清单 11-4 自动压缩 HTTP 消息](src/main/java/com/takeseem/demo/netty/chapter11/HttpCompressionInitializer.java)
* [代码清单 11-5 使用 HTTPS](src/main/java/com/takeseem/demo/netty/chapter11/HttpsCodecInitializer.java)
* [代码清单 11-6 在服务器端支持 WebSocket](src/main/java/com/takeseem/demo/netty/chapter11/WebSocketServerInitializer.java)
* [代码清单 11-7 发送心跳](src/main/java/com/takeseem/demo/netty/chapter11/IdleStateHandlerInitializer.java)
* 其他代码清单和其他实例差不多，就不再过度练习

### 第 12 章 WebSocket
* [代码清单 12-1 HTTPRequestHandler](src/main/java/com/takeseem/demo/netty/chapter12/HTTPRequestHandler.java)
* [代码清单 12-2 处理文本帧](src/main/java/com/takeseem/demo/netty/chapter12/TextWebSocketFrameHandler.java)
* [代码清单 12-3 初始化 ChannelPipeline](src/main/java/com/takeseem/demo/netty/chapter12/ChatServerInitializer.java)
* [代码清单 12-4 引导服务器](src/main/java/com/takeseem/demo/netty/chapter12/ChatServer.java)
* [代码清单 12-6 为 ChannelPipeline 添加加密](src/main/java/com/takeseem/demo/netty/chapter12/SecureChatServerInitializer.java)
* [代码清单 12-7 向 ChatServer 添加加密](src/main/java/com/takeseem/demo/netty/chapter12/SecureChatServer.java)
  * 添加加密特性后，就不能使用zero-copy，所以使用chunked方式传文件

### 第 13 章 使用UDP广播事件 
 * [代码清单 13-1 LogEvent 消息](src/main/java/com/takeseem/demo/netty/chapter13/LogEvent.java)
 * [代码清单 13-2 LogEventEncoder](src/main/java/com/takeseem/demo/netty/chapter13/LogEventEncoder.java)
 * [代码清单 13-3 LogEventBroadcaster](src/main/java/com/takeseem/demo/netty/chapter13/LogEventBroadcaster.java)
 * [代码清单 13-6 LogEventDecoder](src/main/java/com/takeseem/demo/netty/chapter13/LogEventDecoder.java)
 * [代码清单 13-7 LogEventHandler](src/main/java/com/takeseem/demo/netty/chapter13/LogEventHandler.java)
 * [代码清单 13-8 LogEventMonitor](src/main/java/com/takeseem/demo/netty/chapter13/LogEventMonitor.java)

### 第 14 章 案例研究,第一部分
 * 14.1 Droplr——构建移动服务：Netty做管道的例子很有意思，总体上来说可以参考的
   * 并发能力：几百到几千/s
   * cpu: 很少超过5%
   * 内存：-Xms1G -Xmx2G，基本上是1G
 * 14.2 Firebase—— 实时的数据同步服务
   * 相同端口的不同协议支持：http、websocket、ssl，我想如果实现：不同域名的https支持也能实现
   * 流量统计：入站和出站
   * 性能从来不是问题
   * netty的解耦（业务和网络）恰到好处
 * 14.3 Urban Airship—— 构建移动服务
   * 轻松编写与APNS对接的二进制协议
   * 自建消息通道：单进程4G的堆能支持近百万持久化TCP连接
   * Urban Airship 小结—跨越防火墙边界
     * 内部RPC框架通讯基础
     * 性能和负载测试：得益于Netty强大的性能
     * 同步协议的异步客户端
     
### 第 15 章 案例研究,第二部分
 * Netty 在 Facebook 的使用: Nifty 和 Swift
   * Nifty一套RPC客户端/服务器，构建在Netty和Thrift之上
   * Swift:一种更快的构建 Java Thrift 服务的方式
   * 性能：和C++构建的能保持在一个数量级，稳定性强
 * Netty 在 Twitter 的使用: Finagle，API端点从Ruby切换到Netty后
   * 延迟从数百毫秒降低到数十毫秒
   * 机器从3位数减少到个位数
   * 推特消息记录(2013年)：143,199/s

### 感受
 * 架构简单、设计精妙、线程约定精妙绝伦、大量已实现的ChannelHandler
 * 异步服务器、API网关
 * 与nginx相比的想法：我对nginx代码无研究，也只是用用而已，所以提出的一些感受可能不正确
   * nginx的目标是网络服务器、而且发展多年：网络层面和业务方面耦合紧密
   * netty设计时定位就是屏蔽网络层面的复杂性，设计确实精妙所以如果涉及网络层面时相比基于nginx来做更加灵活
   * 比如：API网关https支持，可以轻松做到多域名(多租户)不同证书，这里的租户可能多达几万，而nginx需要配置很多很多