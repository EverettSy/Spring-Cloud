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

* [1.Spring Cloud系列之组件介绍](architecture/1.Spring%20Cloud系列之组件介绍.md)
* [2.Spring Cloud系列之Commons](architecture/2.Spring%20Cloud系列之Commons.md)
* [3.Spring Cloud系列之OpenFeign组件](architecture/3.Spring%20Cloud系列之OpenFeign组件.md)
* [4.Spring Cloud系列之OpenFeign的生命周期-创建代理](architecture/4.Spring%20Cloud系列之OpenFeign的生命周期-创建代理.md)
* [5.Spring Cloud系列之LoadBalancer简介](architecture/5.Spring%20Cloud系列之LoadBalancer简介.md)
* [6.Spring Cloud系列之LoadBalancer核心源码](architecture/6.Spring%20Cloud系列之LoadBalancer核心源码.md)
* [13.Spring Cloud系列之Eureka架构和核心概念](architecture/13.Eureka架构和核心概念.md)
* [14.Spring Cloud系列之Eureka的实例配置](architecture/14.Eureka的实例配置.md)



## 技术要点篇

## 部署篇

## 工具篇
> 一些常用开发工具的使用

* [RestTemplate 使用简介](tools/restTemplate.md)
* [docker日志命令](tools/docker日志命令.md)
* [Elasticsearch官方已支持SQL查询，用起来贼方便！](tools/Elasticsearch使用SQL查询.md)
* [7.使用Log4j2以及一些核心配置](architecture/container-log/7.使用Log4j2以及一些核心配置.md)
* [8.Log4j2%20监控相关](architecture/container-log/8.Log4j2%20监控相关.md)
* [9.UnderTow%20简介与内部原理](architecture/container-log/9.UnderTow%20简介与内部原理.md)
* [10.UnderTow核心配置](architecture/container-log/10.UnderTow核心配置.md)
* [11.UnderTow AccessLog 配置介绍](architecture/container-log/11.UnderTow%20AccessLog%20配置介绍.md)
* [12.UnderTow 订制](architecture/container-log/12.UnderTow%20订制.md)


## 读书笔记篇
* [Raft分布式协议](notes/Raft分布式协议.md)

## 面试题篇