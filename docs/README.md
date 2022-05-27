## 简介
SpringCloud 2020系列是一个完整的微服务学习体系，用户可以通过使用 Spring Cloud 快速搭建一个自己的微服务系统。
包含如下组件：

* 服务发现：DiscoveryClient，从注册中心发现微服务。
* 服务注册：ServiceRegistry，注册微服务到注册中心。
* 负载均衡：LoadBalancerClient，客户端调用负载均衡。其中，重试策略从spring-cloud-commons-2.2.6加入了负载均衡的抽象中。
* 断路器：CircuitBreaker，负责什么情况下将服务断路并降级
* 调用 http 客户端：内部 RPC 调用都是 http 调用

## 序章

## 架构篇
> Spring Cloud系列教程

* [Spring Cloud系列之Commons](architecture/Spring%20Cloud系列之Commons.md)
* [Spring Cloud系列之组件介绍](architecture/Spring%20Cloud系列之组件介绍.md)
* [Spring Cloud系列之OpenFeign组件](architecture/Spring%20Cloud系列之OpenFeign组件.md)
* [Spring Cloud系列之OpenFeign的生命周期-创建代理](architecture/Spring%20Cloud系列之OpenFeign的生命周期-创建代理.md)
* [Spring Cloud系列之LoadBalancer简介](architecture/Spring%20Cloud系列之LoadBalancer简介.md)
* [Spring Cloud系列之LoadBalancer核心源码](architecture/Spring%20Cloud系列之LoadBalancer核心源码.md)

## 业务篇

## 技术要点篇

## 部署篇

## 进阶篇

## 参考篇

## 工具篇
> 一些常用开发工具的使用

* [RestTemplate 使用简介](tools/restTemplate.md)
* [docker日志命令](tools/docker日志命令.md)
* [Elasticsearch官方已支持SQL查询，用起来贼方便！](tools/Elasticsearch使用SQL查询.md)


## 读书笔记
* [Raft分布式协议](notes/Raft分布式协议.md)