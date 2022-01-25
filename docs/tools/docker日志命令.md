## docker logs－查看docker容器日志

```shell
$ docker logs [OPTIONS] CONTAINER
  Options:
        --details        显示更多的信息
    -f, --follow         跟踪实时日志
        --since string   显示自某个timestamp之后的日志，或相对时间，如42m（即42分钟）
        --tail string    从日志末尾显示多少行日志， 默认是all
    -t, --timestamps     显示时间戳
        --until string   显示自某个timestamp之前的日志，或相对时间，如42m（即42分钟）
```

**例子：**

查看指定时间后的日志，只显示最后100行：

```shell
$ docker logs -f -t --since="2018-02-08" --tail=100 CONTAINER_ID
```

查看最近30分钟的日志:

```shell
$ docker logs --since 30m CONTAINER_ID
```

查看某时间之后的日志：

```shell
$ docker logs -t --since="2018-02-08T13:23:37" CONTAINER_ID
```

查看某时间段日志：

```shell
$ docker logs -t --since="2018-02-08T13:23:37" --until "2018-02-09T12:23:37" CONTAINER_ID
```

### 容器日志的输出形式

- stdout 标准输出
- stderr 标准错误
- 以json格式存放在容器对于到日志文件中

### docker日志内容类型

- docker自身运行时Daemon的日志内容
- docker容器的日志内容

### docker logs的实现原理

“Docker Daemon是Docker架构中一个常驻在后台的系统进程，它在后台启动了一个Server，Server负责接受Docker Client发送的请求；接受请求后，Server通过路由与分发调度，找到相应的Handler来执行请求。–《Docker源码分析》”

当我们输入docker logs的时候会转化为Docker Client向Docker Daemon发起请求,Docker Daemon 在运行容器时会去创建一个协程(goroutine)，绑定了整个容器内所有进程的标准输出文件描述符。因此容器内应用的所有只要是标准输出日志，都会被 goroutine 接收，Docker Daemon会根据容器id和日志类型读取日志内容，最终会输出到用户终端上并且通过json格式存放在/var/lib/docker/containers目录下。