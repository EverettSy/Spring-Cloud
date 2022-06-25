# Log4j2 监控相关

![](https://cdn.jsdelivr.net/gh/EverettSy/ImageBed@master/uPic/mei1Yy.png)

Log4j2 异步日志核心通过 RingBuffer 实现，如果某一时刻产生大量日志并且写的速度不及时导致 RingBuffer 满了，业务代码中调用日志记录的地方就会阻塞。所以我们需要对 RingBuffer 进行监控。Log4j2 对于每一个 AsyncLogger 配置，都会创建一个独立的 RingBuffer，例如下面的 Log4j2 配置：

```xml
<!--省略了除了 loggers 以外的其他配置-->
 <loggers>
    <!--default logger -->
    <Asyncroot level="info" includeLocation="true">
        <appender-ref ref="console"/>
    </Asyncroot>
    <AsyncLogger name="RocketmqClient" level="error" additivity="false" includeLocation="true">
        <appender-ref ref="console"/>
    </AsyncLogger>
    <AsyncLogger name="com.alibaba.druid.pool.DruidDataSourceStatLoggerImpl" level="error" additivity="false" includeLocation="true">
        <appender-ref ref="console"/>
    </AsyncLogger>
    <AsyncLogger name="org.mybatis" level="error" additivity="false" includeLocation="true">
        <appender-ref ref="console"/>
    </AsyncLogger>
</loggers>
```

这个配置包含 4 个 AsyncLogger，对于每个 AsyncLogger 都会创建一个 RingBuffer。Log4j2 也考虑到了监控 AsyncLogger 这种情况，所以将 AsyncLogger 的监控暴露成为一个 MBean（JMX Managed Bean）。

相关源码如下：

**Server.java**

```java
private static void registerLoggerConfigs(final LoggerContext ctx, final MBeanServer mbs, final Executor executor) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
  //获取 log4j2.xml 配置中的 loggers 标签下的所有配置值 
  Map<String, LoggerConfig> map = ctx.getConfiguration().getLoggers();
    Iterator var4 = map.keySet().iterator();
	//遍历每个 key，其实就是 logger 的 name
    while(var4.hasNext()) {
        String name = (String)var4.next();
        LoggerConfig cfg = (LoggerConfig)map.get(name);
        LoggerConfigAdmin mbean = new LoggerConfigAdmin(ctx, cfg);
       //对于每个 logger 注册一个 LoggerConfigAdmin
        register(mbs, mbean, mbean.getObjectName());
      //如果是异步日志配置，则注册一个 RingBufferAdmin
        if (cfg instanceof AsyncLoggerConfig) {
            AsyncLoggerConfig async = (AsyncLoggerConfig)cfg;
            RingBufferAdmin rbmbean = async.createRingBufferAdmin(ctx.getName());
            register(mbs, rbmbean, rbmbean.getObjectName());
        }
    }

}
```

创建的 MBean 的类源码：**RingBufferAdmin.java**

```java
public class RingBufferAdmin implements RingBufferAdminMBean {
    private final RingBuffer<?> ringBuffer;
    private final ObjectName objectName;

    public static RingBufferAdmin forAsyncLogger(final RingBuffer<?> ringBuffer, final String contextName) {
        String ctxName = Server.escape(contextName);
        String name = String.format("org.apache.logging.log4j2:type=%s,component=AsyncLoggerRingBuffer", ctxName);
        return new RingBufferAdmin(ringBuffer, name);
    }

  //创建 RingBufferAdmin，名称格式符合 Mbean 的名称格式
    public static RingBufferAdmin forAsyncLoggerConfig(final RingBuffer<?> ringBuffer, final String contextName, final String configName) {
        String ctxName = Server.escape(contextName);
      //对于 RootLogger，这里 cfgName 为空字符串  
        String cfgName = Server.escape(configName);
        String name = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s,subtype=RingBuffer", ctxName, cfgName);
        return new RingBufferAdmin(ringBuffer, name);
    }

    protected RingBufferAdmin(final RingBuffer<?> ringBuffer, final String mbeanName) {
        this.ringBuffer = ringBuffer;

        try {
            this.objectName = new ObjectName(mbeanName);
        } catch (Exception var4) {
            throw new IllegalStateException(var4);
        }
    }

  //获取 RingBuffer 的大小
    public long getBufferSize() {
        return this.ringBuffer == null ? 0L : (long)this.ringBuffer.getBufferSize();
    }
  //获取 RingBuffer 剩余的大小
    public long getRemainingCapacity() {
        return this.ringBuffer == null ? 0L : this.ringBuffer.remainingCapacity();
    }

    public ObjectName getObjectName() {
        return this.objectName;
    }

```

我们的微服务项目中使用了 spring boot，并且集成了 prometheus。我们可以通过将 Log4j2 RingBuffer 大小作为指标暴露到 prometheus 中，通过如下代码：

```java
package com.syrobin.cloud.common.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.jmx.RingBufferAdminMBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.export.ConditionalOnEnabledMetricsExport;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-06-01 22:22
 */
@Log4j2
@Configuration(proxyBeanMethods = false)
//需要在引入了 prometheus 并且 actuator 暴露了 prometheus 端口的情况下才加载
@ConditionalOnEnabledMetricsExport("prometheus")
public class Log4j2Configuration {

    @Autowired
    private ObjectProvider<PrometheusMeterRegistry> meterRegistry;

    //只初始化一次
    private volatile boolean isInitialized = false;


    //需要在 ApplicationContext 刷新之后进行注册
    //在加载 ApplicationContext 之前，日志配置就已经初始化好了
    //但是 prometheus 的相关 Bean 加载比较复杂，并且随着版本更迭改动比较多，所以就直接偷懒，在整个 ApplicationContext 刷新之后再注册
    // ApplicationContext 可能 refresh 多次，例如调用 /actuator/refresh，还有就是多 ApplicationContext 的场景
    // 这里为了简单，通过一个简单的 isInitialized 判断是否是第一次初始化，保证只初始化一次
    @EventListener(ContextRefreshedEvent.class)
    public synchronized void init() {
        if (!isInitialized) {
            //通过 LogManager 获取 LoggerContext，从而获取配置
            LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
            org.apache.logging.log4j.core.config.Configuration configuration = loggerContext.getConfiguration();
            //获取 LoggerContext 的名称，因为 Mbean 的名称包含这个
            String ctxName = loggerContext.getName();
            configuration.getLoggers().keySet().forEach(k -> {
                try {
                    //针对 RootLogger，它的 cfgName 是空字符串，为了显示好看，我们在 prometheus 中将它命名为 root
                    String cfgName = StringUtils.isBlank(k) ? "" : k;
                    String gaugeName = StringUtils.isBlank(k) ? "" : k;
                    Gauge.builder(gaugeName + "_logger_ring_buffer_remaining_capacity", () ->
                    {
                        try {
                            return (Number) ManagementFactory.getPlatformMBeanServer()
                                    .getAttribute(new ObjectName(
                                            //按照 Log4j2 源码中的命名方式组装名称
                                            String.format(RingBufferAdminMBean.PATTERN_ASYNC_LOGGER_CONFIG, cfgName, ctxName)
                                            //获取剩余大小，注意这个是严格区分大小写的
                                    ), "RemainingCapacity");
                        } catch (Exception e) {
                            log.error("get {} ring buffer remaining size error", k, e);
                        }
                        return -1;
                    }).register(meterRegistry.getIfAvailable());
                } catch (Exception e) {
                    log.error("Log4j2Configuration-init error: {}", e.getMessage(), e);
                }
            });
            isInitialized = true;
        }
    }
}
```

增加这个代码之后，请求 `/actuator/prometheus` 之后，可以看到对应的返回：

```java
//省略其他的
# HELP root_logger_ring_buffer_remaining_capacity  
# TYPE root_logger_ring_buffer_remaining_capacity gauge
root_logger_ring_buffer_remaining_capacity 262144.0
# HELP org_mybatis_logger_ring_buffer_remaining_capacity  
# TYPE org_mybatis_logger_ring_buffer_remaining_capacity gauge
org_mybatis_logger_ring_buffer_remaining_capacity 262144.0
# HELP com_alibaba_druid_pool_DruidDataSourceStatLoggerImpl_logger_ring_buffer_remaining_capacity  
# TYPE com_alibaba_druid_pool_DruidDataSourceStatLoggerImpl_logger_ring_buffer_remaining_capacity gauge
com_alibaba_druid_pool_DruidDataSourceStatLoggerImpl_logger_ring_buffer_remaining_capacity 262144.0
# HELP RocketmqClient_logger_ring_buffer_remaining_capacity  
# TYPE RocketmqClient_logger_ring_buffer_remaining_capacity gauge
RocketmqClient_logger_ring_buffer_remaining_capacity 262144.0
```

这样，当这个值为 0 持续一段时间后（就代表 RingBuffer 满了，日志生成速度已经远大于消费写入 Appender 的速度了），我们就认为这个应用日志负载过高了。

![](https://cdn.jsdelivr.net/gh/EverettSy/ImageBed@master/uPic/uEThem.png)

其实可以通过 JMX 直接查看动态修改 Log4j2 的各种配置，Log4j2 中暴露了很多 JMX Bean，例如通过 JConsole 可以查看并修改： 

但是，JMX 里面包含的信息太多，并且我们的服务器在世界各地，远程 JMX 很不稳定，所以我们还是通过 actuator 暴露 http 接口进行操作。

首先，要先配置 actuator 要通过 HTTP 暴露出日志 API，我们这里的配置是：

```yaml
management:
  endpoints:
    # 不通过 JMX 暴露任何 actuator 接口
    jmx:
      exposure:
        exclude: '*'
    # 通过 JMX 暴露所有 actuator 接口
    web:
      exposure:
        include: '*'
```

请求接口 `GET /actuator/loggers`，可以看到如下的返回，可以知道当前日志框架支持哪些级别的日志配置，以及每个 Logger 的级别配置。

```json
{
	"levels": [
		"OFF",
		"FATAL",
		"ERROR",
		"WARN",
		"INFO",
		"DEBUG",
		"TRACE"
	],
	"loggers": {
		"ROOT": {
			"configuredLevel": "WARN",
			"effectiveLevel": "WARN"
		},
		"org.mybatis": {
			"configuredLevel": "ERROR",
			"effectiveLevel": "ERROR"
		}
	},
	"groups": {
	}
}
```

如果我们想增加或者修改某一 Logger 的配置，可以通过 `POST /actuator/loggers/自定义logger名称`，Body 为：

```json
{
	"configuredLevel": "WARN"
}
```
