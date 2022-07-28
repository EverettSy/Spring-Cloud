# 12.UnderTow 简介与内部原理

![](https://cdn.jsdelivr.net/gh/EverettSy/ImageBed@master/uPic/t7gMNZ.png)

在我们的项目中，我们没有采用默认的 Tomcat 容器，而是使用了 UnderTow 作为我们的容器。其实性能上的差异并没有那么明显，但是使用 UnderTow 我们可以利用直接内存作为网络传输的 buffer，减少业务的 GC，优化业务的表现。其实 Tomcat 也有使用直接内存作为网络传输的 buffer 的配置，即 Connector 使用 NIO 或者 NIO2，还有 APR 这种基于 JNI 的优化文件与请求传输的方式，但是 tomcat 随着不断迭代与发展，功能越来越完善以及组件化的同时，架构也越来越复杂，这也带来了代码设计与质量上的一些降低。对比 Tomcat Connector 那里的源代码与设计，我最终选择了更为轻量设计的 Undertow。至于不选 Jetty 的原因和 Tomcat 类似，不选 reactor-netty 的主要原因是项目还是比较新并且不太成熟，并且基于异步回调，很多时候异常处理不全面，导致最后诡异的响应并且异常定位成本比较高。

Undertow 的官网：https://undertow.io/

- 红帽开源产品，目前是 WildFly（JBoss AS）的默认 Web 容器。
- 在不需要非常复杂的配置情况下，可以展现出很高的性能以及稳定性。
- 非常轻量，只需通过 API 即可快速搭建 Web 服务器。
- 底层基于 XNIO，和 Netty 设计类似，使用 NIO 作为网络交互的方式，并且使用直接内存作为网络传输的 buffer，减少业务的 GC。
- 由于基于这种异步框架，所以配置也是交由链式Handler配置和处理请求，可以最小化按需加载模块，无须加载多余功能
- 支持 Servlet 4.0 以及向下兼容，支持 WebSocket

但是，Undertow 有一些令人担忧的地方：

1. **官网一股贫穷的气息**，背靠红帽这个不太靠谱的爹
2. NIO 框架采用的是 XNIO，**在官网 3.0 roadmap 声明中提到了将会在 3.0 版本开始，从 XNIO 迁移到 netty**， 参考：Undertow 3.0 Announcement。但是，目前已经过了快两年了，3.0 还是没有发布，并且 github 上 3.0 的分支已经一年多没有更新了。目前，还是在用 2.x 版本的 Undertow。不知道是 3.0 目前没必要开发，还是胎死腹中了呢？目前国内的环境对于 netty 使用更加广泛并且大部分人对于 netty 更加熟悉一些， XNIO 应用并不是很多。不过，XNIO 的设计与 netty 大同小异。
3. **官方文档的更新比较慢，可能会慢 1~2 个小版本**，导致 Spring Boot 粘合 Undertow 的时候，配置显得不会那么优雅。参考官方文档的同时，最好还是看一下源码，至少看一下配置类，才能搞懂究竟是怎么设置的。
4. 仔细看 Undertow 的源码，会发现有很多防御性编程的设计或者功能性设计 Undertow 的作者想到了，但是就是没实现，有很多没有实现的半成品代码。这也令人担心 Underow 是否开发动力不足，哪一天会突然死掉？

不过，幸好有 spring-boot，在 spring-boot 项目中，切换容器的成本不大，修改依赖即可。同时要注意，不同 web 容器的配置。

![](https://cdn.jsdelivr.net/gh/EverettSy/ImageBed@master/uPic/3oeSWT.png)

Undertow 目前(2.x) 还是基于 Java XNIO，Java XNIO 是一个对于 JDK NIO 类的扩展，和 netty 的基本功能是一样的，但是 netty 更像是对于 Java NIO 的封装，Java XNIO 更像是扩展封装。主要是 netty 中基本传输承载数据的并不是 Java NIO 中的 `ByteBuffer`，而是自己封装的 `ByteBuf`，而 Java XNIO 各个接口设计还是基于 `ByteBuffer` 为传输处理单元。设计上也很相似，都是 Reactor 模型的设计。其结构如下所示：

![](https://cdn.jsdelivr.net/gh/EverettSy/ImageBed@master/uPic/LdxPkU.png)

Java XNIO 主要包括如下几个概念：

- Java NIO `ByteBuffer`：`Buffer` 是一个具有状态的数组，用来承载数据，可以追踪记录已经写入或者已经读取的内容。主要属性包括：capacity(Buffer 的容量)，position(下一个要读取或者写入的位置下标)，limit(当前可以写入或者读取的极限位置)。程序必须通过将数据放入 Buffer，才能从 Channel 读取或者写入数据。`ByteBuffer`是更加特殊的 Buffer，它可以以直接内存分配，这样 JVM 可以直接利用这个 Bytebuffer 进行 IO 操作，省了一步复制（具体可以参考我的一篇文章：Java 堆外内存、零拷贝、直接内存以及针对于NIO中的FileChannel的思考）。也可以通过文件映射内存直接分配，即 Java MMAP（具体可以参考我的一篇文章：JDK核心JAVA源码解析（5） - JAVA File MMAP原理解析）。所以，一般的 IO 操作都是通过 ByteBuffer 进行的。

- Java NIO `Channel`：Channel 是 Java 中对于打开和某一外部实体（例如硬件设备，文件，网络连接 socket 或者可以执行 IO 操作的某些组件）连接的抽象。Channel 主要是 IO 事件源，所有写入或者读取的数据都必须经过 Channel。对于 NIO 的 Channel，会通过 `Selector` 来通知事件的就绪（例如读就绪和写就绪），之后通过 Buffer 进行读取或者写入。

- XNIO `Worker`: Worker 是 Java XNIO 框架中的基本网络处理单元，一个 Worker 包含两个不同的线程池类型，分别是：

- - 读线程：处理读事件的回调
  - 写线程：处理写事件的回调
  - IO 线程池，主要调用`Selector.start()`处理对应事件的各种回调，原则上不能处理任何阻塞的任务，因为这样会导致其他连接无法处理。IO 线程池包括两种线程（在 XNIO 框架中，通过设置 WORKER_IO_THREADS 来设置这个线程池大小，默认是一个 CPU 一个 IO 线程）：
  - Worker 线程池，处理阻塞的任务，在 Web 服务器的设计中，一般将调用 servlet 任务放到这个线程池执行（在 XNIO 框架中，通过设置 WORKER_TASK_CORE_THREADS 来设置这个线程池大小）

- XNIO `ChannelListener`：ChannelListener 是用来监听处理 Channel 事件的抽象，包括：`channel readable`, `channel writable`, `channel opened`, `channel closed`, `channel bound`, `channel unbound`

Undertow 是基于 XNIO 的 Web 服务容器。在 XNIO 的基础上，增加：

- Undertow `BufferPool`: 如果每次需要 ByteBuffer 的时候都去申请，对于堆内存的 ByteBuffer 需要走 JVM 内存分配流程（TLAB -> 堆），对于直接内存则需要走系统调用，这样效率是很低下的。所以，一般都会引入内存池。在这里就是 `BufferPool`。目前，UnderTow 中只有一种 `DefaultByteBufferPool`，其他的实现目前没有用。这个 DefaultByteBufferPool 相对于 netty 的 ByteBufArena 来说，非常简单，类似于 JVM TLAB 的机制（可以参考我的另一系列：全网最硬核 JVM TLAB 分析），但是简化了很多。我们只需要配置 buffer size ，并开启使用直接内存即可。
- Undertow `Listener`: 默认内置有 3 种 Listener ，分别是 HTTP/1.1、AJP 和 HTTP/2 分别对应的 Listener（HTTPS 通过对应的 HTTP Listner 开启 SSL 实现），负责所有请求的解析，将请求解析后包装成为 `HttpServerExchange` 并交给后续的 `Handler` 处理。
- Undertow `Handler`: 通过 Handler 处理响应的业务，这样组成一个完整的 Web 服务器。

## 总结

!>我们这一节详细介绍了 Undertow 的结构，并且说明了 Undertow 的优点以及让我们比较担心的地方，并且与其他的 Web 容器做了对比。下一节我们将会分析 Undertow 的详细配置。