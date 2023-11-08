##   Redis 集群模式简述

一个集群模式的官方推荐最小最佳实践方案是 6 个节点，3 个 Master 3 个 Slave 的模式，如下图所示。

![](https://cdn.jsdelivr.net/gh/EverettSy/ImageBed@master/uPic/c16HWa.png)


##  key 分槽与转发机制

Redis 将键空间分为了 16384 个槽，通过以下算法确定每一个 key 的槽：
```
CRC16(key) mod 16384
```

由于 16384 = 2 的 14 次方，对一个 2 的 n 次方取余相当于对于它的 2 的 n 次方减一取与运算。所以优化为：

```
CRC16(key) & 16383
```

当key 包含hash tags 的时候（例如 key{sub}1）, 会以sub tags 中指定的字符串（就是sub）计算槽，所以key{sub}1 和 key{sub}2 会到同一个槽中。

客户端可以发送读取任一个槽的命令到一个集群实例，当槽属于请求的实例的时候，就会处理，否则会告诉客户端这个槽在哪里，例如如果将下面命令发送到第二个Master：
```
GET key1
返回：MOVED slot ip:port (第一个Master)
```
默认情况下，所有的命令只能发送到Master。如果需要使用Slave 处理读请求，需要先在客户端执行  `readonly`

## 主从自动切换机制

当一个Master 发生故障，如果有Slave，则会切换为 Slave。

如何判断 Master 发生故障了呢？Redis 集群配置中有一个配置，`cluster-node-timeout`集群心跳超时时间。当集群内节点建立连接后，定时任务 clusterCron 函数（参考源码：https://github.com/redis/redis/blob/6.0/src/cluster.c）会每隔一秒随机选择一个节点发送心跳。如果在超时时间（`cluster-node-timeout`）的时间内未收到心跳响应，则将这个节点标记为 pfail。

如果集群中**有一半以上的Master** 标记一个节点的状态是pfail，那么这个节点标记为fail。

当节点变为fail 就会触发自动主从切换。主从切换的过程，也涉及到类似的选举：
1. 当某个 Master 被标记为fail 之后，对应的 Slave 节点执行定时任务 currentEpoch 函数时，选取复制偏移量，也就是主从同步进度最大、数据最新的 Slave 尝试变为主。
2. 这个 Slave 设置自己的 currentEpoch + 1 （正常情况下集群中所有的 currentEpoch 相同，每次选举都会加 1，并且每个 currentEpoch 只能投一次 ，防止多个 Slave 同时发起选举后难以获取大多数票），之后向所有的 Master 发送 failover 请求，如果得到大多数 Master 的同意则开始执行主从切换。

## 集群不可用情况

根据上面的描述，我们可以总结出如下不可用的情况

1. 当访问一个 Master 和 Slave 节点都挂了的槽的时候，会报槽无法获取。
2. 当集群 Master 节点个数小于 3 个的时候，或者集群可用节点个数为偶数的时候，基于fail 的这种选举机制的自动主从切换过程可能会不能正常工作，一个是标记 fail 的过程，一个是 选举新的 Master 的过程，都有异常。

## 参考

1. 官方文档：https://redis.io/topics/cluster-spec

2. 源代码：https://github.com/redis/redis

3. 知道 Redis-Cluster 么？说说其中可能不可用的情况：https://mp.weixin.qq.com/s/-DMg8eJ4r2DSawsBR0SOPQ

