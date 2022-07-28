# Spring Cloud LoadBalancer背景

Spring Cloud LoadBalancer是一个客户端负载均衡器，类似于Ribbon，但是由于Ribbon已经进入维护模式，并且Ribbon 2并不与Ribbon 1相互兼容，所以Spring Cloud全家桶在Spring Cloud Commons项目中，添加了Spring cloud Loadbalancer作为新的负载均衡器，并且做了向前兼容，就算你的项目中继续用 Spring Cloud Netflix 套装（包括Ribbon，Eureka，Zuul，Hystrix等等）让你的项目中有这些依赖，你也可以通过简单的配置，把ribbon替换成Spring Cloud LoadBalancer。



# 负载均衡器在哪里使用？

Spring Cloud 中内部微服务调用默认是http 请求，主要通过下面三种API：

* RestTemplate：同步 http API
* WebClient：异步响应式http API
* 三方客户端封装，例如openFeign

如果项目中加入了 spring-cloud-loadbalancer 的依赖并且配置启用了，那么会自动在相关的 Bean 中加入负载均衡器的特性。

- 对于 RestTemplate，会自动对所有 `@LoadBalanced` 注解修饰的 RestTemplate Bean 增加 Interceptor 从而加上了负载均衡器的特性。
- 对于 WebClient，会自动创建ReactorLoadBalancerExchangeFilterFunction，我们可以通过加入ReactorLoadBalancerExchangeFilterFunction 会加入负载均衡器的特性
- 对于三方客户端，一般不需要我们额外配置什么。

# Spring Cloud LoadBalancer 结构简介

Spring Cloud LoadBalancer 使用了**NamedContextFactory** 机制实现了不同微服务使用不同的Spring Cloud LoadBalancer 配置。相关核心实现是`@LoadBalancerClient` 和 `@LoadBalancerClients` 这两个注解，以及NamedContextFactory.Specification 的实现LoadBalancerClientSpecification，NamedContextFactory` 的实现 `LoadBalancerClientFactory。如下图所示

![](https://cdn.jsdelivr.net/gh/EverettSy/ImageBed@master/uPic/jAnxiT.png)

1. 可以通过 `loadbalancer.client.name` 这个属性获取当前要创建的 Bean 是哪个微服务的

2. 可以知道默认配置 LoadBalancerClientConfiguration，再查看它里面的源代码我们可以知道主要初始化两个 Bean：

   i. ReactorLoadBalancer，负载均衡器，因为有 `@ConditionalOnMissingBean` 所以可以被替换，这就是我们的扩展点

   ii. ServiceInstanceSupplier，提供实例信息的 Supplier，因为有 `@ConditionalOnMissingBean` 所以可以被替换，这就是我们的扩展点

3. Specification 为 LoadBalancerSpecification，再分析其调用可以知道，可以通过 `@LoadBalancerClient` 和 `@LoadBalancerClients` 在 `LoadBalancerClientConfiguration` 的基础上额外指定配置。

   

| 注解                      | 作用                                                    | 常用 |
| :------------------------ | :------------------------------------------------------ | :--: |
| @ConditionalOnProperty    | 当指定的属性有指定的值时进行实例化                      |  ☆   |
| @ConditionalOnMissingBean | 当容器里没有指定 Bean 的条件下进行实例化。              |  ☆   |
| @ConditionalOnExpression  | 基于 SpEL 表达式的条件判断，多条件                      |  ☆   |
| @ConditionalOnBean        | 仅仅在当前上下文中存在某个对象时，才会实例化一个 Bean。 |      |
