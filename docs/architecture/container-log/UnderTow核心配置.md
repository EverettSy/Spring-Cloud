# UnderTow 核心配置

![](https://cdn.jsdelivr.net/gh/EverettSy/ImageBed@master/uPic/Vc4Xjl.png)

Undertow 的配置可以参考 Undertow 的 Builder，并且其中也有一些默认的配置参数：

**Undertow**

```java
private Builder() {
            this.listeners = new ArrayList();
            this.workerOptions = OptionMap.builder();
            this.socketOptions = OptionMap.builder();
            this.serverOptions = OptionMap.builder();
            this.ioThreads = Math.max(Runtime.getRuntime().availableProcessors(), 2);
            this.workerThreads = this.ioThreads * 8;
            long maxMemory = Runtime.getRuntime().maxMemory();
            if (maxMemory < 67108864L) {
                this.directBuffers = false;
                this.bufferSize = 512;
            } else if (maxMemory < 134217728L) {
                this.directBuffers = true;
                this.bufferSize = 1024;
            } else {
                this.directBuffers = true;
                this.bufferSize = 16364;
            }

        }
```

* ioThreads 大小为可用CPU数量* 2，即Undertow的XNIO 的读线程个数为可用CPU数量，写线程个数也为可用CPU数量。
* workerThreads 大小为ioThreads * 8
* 如果内存大小小于64MB，则不使用直接内存，bufferSize 为512字节
* 如果内存大小大于64MB小于128MB，则使用直接内存，bufferSize 为 1024 字节
* 如果内存大小大于 128 MB，则使用直接内存，bufferSize 为 16 KB 减去 20 字节，这 20 字节用于协议头。

![](https://cdn.jsdelivr.net/gh/EverettSy/ImageBed@master/uPic/DuxOU8.png)

**DefaultByteBufferPool** 构造器：

```java
public DefaultByteBufferPool(boolean direct, int bufferSize, int maximumPoolSize, int threadLocalCacheSize, int leakDecetionPercent) {
    this.threadLocalCache = new ThreadLocal();
    this.threadLocalDataList = new ArrayList();
    this.queue = new ConcurrentLinkedQueue();
    this.currentQueueLength = 0;
    this.reclaimedThreadLocals = 0;
    this.direct = direct;
    this.bufferSize = bufferSize;
    this.maximumPoolSize = maximumPoolSize;
    this.threadLocalCacheSize = threadLocalCacheSize;
    this.leakDectionPercent = leakDecetionPercent;
    if (direct) {
        this.arrayBackedPool = new DefaultByteBufferPool(false, bufferSize, maximumPoolSize, 0, leakDecetionPercent);
    } else {
        this.arrayBackedPool = this;
    }

}
```

其中：

* direct：是否使用直接内存，我们需要设置为true，来使用直接内存
* bufferSize：每次申请的buffer大小，我们主要要考虑这个大小
* maximumPoolSize：buffer 池最大大小，一般不用修改
* threadLocalCacheSize：线程本地 buffer 池大小，一般不用修改
* leakDecetionPercent：内存泄漏检查百分比，目前没什么用

对于bufferSize，最好和你系统的 TCP Socket Buffer 配置一样。在我们的容器中，我们将微服务实例的容器内的 TCP Socket Buffer 的读写 buffer 大小成一模一样的配置（因为微服务之间调用，发送的请求也是另一个微服务接受，所以调整所有微服务容器的读写 buffer 大小一致，来优化性能，默认是根据系统内存来自动计算出来的）。

查看 Linux 系统 TCP Socket Buffer 的大小：

- `/proc/sys/net/ipv4/tcp_rmem` (对于读取)
- `/proc/sys/net/ipv4/tcp_wmem` (对于写入)

在我们的容器中，分别是：

![](https://cdn.jsdelivr.net/gh/EverettSy/ImageBed@master/uPic/Pkmd5p.png)

从左到右三个值分别为：每个 TCP Socket 的读 Buffer 与写 Buffer 的大小的 最小值，默认值和最大值，单位是字节。

我们设置我们 Undertow 的 buffer size 为 TCP Socket Buffer 的默认值，即 16 KB。Undertow 的 Builder 里面，如果内存大于 128 MB，buffer size 为 16 KB 减去 20 字节（为协议头预留）。所以，我们使用默认的即可。

**application.yml** 配置：

```yaml
server.undertow:
    # 是否分配的直接内存(NIO直接分配的堆外内存)，这里开启，所以java启动参数需要配置下直接内存大小，减少不必要的GC
    # 在内存大于 128 MB 时，默认就是使用直接内存的
    directBuffers: true
    # 以下的配置会影响buffer,这些buffer会用于服务器连接的IO操作
    # 如果每次需要 ByteBuffer 的时候都去申请，对于堆内存的 ByteBuffer 需要走 JVM 内存分配流程（TLAB -> 堆），对于直接内存则需要走系统调用，这样效率是很低下的。
    # 所以，一般都会引入内存池。在这里就是 `BufferPool`。
    # 目前，UnderTow 中只有一种 `DefaultByteBufferPool`，其他的实现目前没有用。
    # 这个 DefaultByteBufferPool 相对于 netty 的 ByteBufArena 来说，非常简单，类似于 JVM TLAB 的机制
    # 对于 bufferSize，最好和你系统的 TCP Socket Buffer 配置一样
    # `/proc/sys/net/ipv4/tcp_rmem` (对于读取)
    # `/proc/sys/net/ipv4/tcp_wmem` (对于写入)
    # 在内存大于 128 MB 时，bufferSize 为 16 KB 减去 20 字节，这 20 字节用于协议头
    buffer-size: 16384 - 20
```

![](https://cdn.jsdelivr.net/gh/EverettSy/ImageBed@master/uPic/BgolaZ.png)

Worker 配置其实就是XNIO的核心配置，主要需要配置的即io 线程池以及worker线程池大小。

默认情况下，io线程池大小为可用CPU数量 * 2，即读线程个数为可用CPU数量，写线程个数也为可用CPU数量。worker线程池大小为io线程池大小 * 8

微服务应用由于涉及的阻塞操作比较多，所以讲worker线程池大小调大一些，我们的应用设置为io 线程池大小 * 32.

**application.yml** 配置：

```yaml
server.undertow.threads:
    # 设置IO线程数, 它主要执行非阻塞的任务,它们会负责多个连接, 默认设置每个CPU核心一个读线程和一个写线程
    io: 16
    # 阻塞任务线程池, 当执行类似servlet请求阻塞IO操作, undertow会从这个线程池中取得线程
    # 它的值设置取决于系统线程执行任务的阻塞系数，默认值是IO线程数*8
    worker: 128
```

![](https://cdn.jsdelivr.net/gh/EverettSy/ImageBed@master/uPic/fv2liI.png)

Spring Boot 中对于 Undertow 相关配置的抽象是 `ServerProperties` 这个类。目前 Undertow 涉及的所有配置以及说明如下（不包括 accesslog 相关的，accesslog 会在下一节详细分析）：

```yaml
server:
  undertow:
    # 以下的配置会影响buffer,这些buffer会用于服务器连接的IO操作
    # 如果每次需要 ByteBuffer 的时候都去申请，对于堆内存的 ByteBuffer 需要走 JVM 内存分配流程（TLAB -> 堆），对于直接内存则需要走系统调用，这样效率是很低下的。
    # 所以，一般都会引入内存池。在这里就是 `BufferPool`。
    # 目前，UnderTow 中只有一种 `DefaultByteBufferPool`，其他的实现目前没有用。
    # 这个 DefaultByteBufferPool 相对于 netty 的 ByteBufArena 来说，非常简单，类似于 JVM TLAB 的机制
    # 对于 bufferSize，最好和你系统的 TCP Socket Buffer 配置一样
    # `/proc/sys/net/ipv4/tcp_rmem` (对于读取)
    # `/proc/sys/net/ipv4/tcp_wmem` (对于写入)
    # 在内存大于 128 MB 时，bufferSize 为 16 KB 减去 20 字节，这 20 字节用于协议头
    buffer-size: 16364
    # 是否分配的直接内存(NIO直接分配的堆外内存)，这里开启，所以java启动参数需要配置下直接内存大小，减少不必要的GC
    # 在内存大于 128 MB 时，默认就是使用直接内存的
    directBuffers: true
    threads:
      # 设置IO线程数, 它主要执行非阻塞的任务,它们会负责多个连接, 默认设置每个CPU核心一个读线程和一个写线程
      io: 4
      # 阻塞任务线程池, 当执行类似servlet请求阻塞IO操作, undertow会从这个线程池中取得线程
      # 它的值设置取决于系统线程执行任务的阻塞系数，默认值是IO线程数*8
      worker: 128
    # http post body 大小，默认为 -1B ，即不限制
    max-http-post-size: -1B
    # 是否在启动时创建 filter，默认为 true，不用修改
    eager-filter-init: true
    # 限制路径参数数量，默认为 1000
    max-parameters: 1000
    # 限制 http header 数量，默认为 200
    max-headers: 200
    # 限制 http header 中 cookies 的键值对数量，默认为 200
    max-cookies: 200
    # 是否允许 / 与 %2F 转义。/ 是 URL 保留字,除非你的应用明确需要，否则不要开启这个转义，默认为 false
    allow-encoded-slash: false
    # 是否允许 URL 解码，默认为 true，除了 %2F 其他的都会处理
    decode-url: true
    # url 字符编码集，默认是 utf-8
    url-charset: utf-8
    # 响应的 http header 是否会加上 'Connection: keep-alive'，默认为 true
    always-set-keep-alive: true
    # 请求超时，默认是不超时，我们的微服务因为可能有长时间的定时任务，所以不做服务端超时，都用客户端超时，所以我们保持这个默认配置
    no-request-timeout: -1
    # 是否在跳转的时候保持 path，默认是关闭的，一般不用配置
    preserve-path-on-forward: false
    options:
      # spring boot 没有抽象的 xnio 相关配置在这里配置，对应 org.xnio.Options 类
      socket:
        SSL_ENABLED: false
      # spring boot 没有抽象的 undertow 相关配置在这里配置，对应 io.undertow.UndertowOptions 类
      server:
        ALLOW_UNKNOWN_PROTOCOLS: false
```

Spring Boot 并没有将所有的 Undertow 与 XNIO 配置进行抽象，如果你想自定义一些相关配置，可以通过上面配置最后的 `server.undertow.options`进行配置。`server.undertow.options.socket` 对应 XNIO 的相关配置，配置类是 `org.xnio.Options`;`server.undertow.options.server` 对应 Undertow 的相关配置，配置类是 `io.undertow.UndertowOptions`。

## 总结

我们这一节详细介绍了 Undertow 的核心配置，主要包括线程池以及 buffer 配置，以及关于 http 协议的一些配置。并且我们还介绍了这些配置在 spring boot 下该如何配置。下一节，我们将详细介绍如何配置 Undertow 的 accesslog。
