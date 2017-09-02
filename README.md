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