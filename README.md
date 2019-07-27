# 使用方式

- QMQ - https://github.com/qunarcorp/qmq
- Spring Boot Starter for QMQ - https://github.com/wjtree/qmq-spring-boot-starter

## 引入 Maven 依赖（中央仓库）

```
<dependency>
    <groupId>xin.wjtree.qmq</groupId>
    <artifactId>qmq-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## 添加 Spring Boot 配置（YML）

```
spring:
  application:
    name: qmq-demo
  qmq:
    # 应用标识 appcode，必填
    app-code: qmq-demo
    # 服务器地址 metaserver，必填
    meta-server: http://127.0.0.1:8080/meta/address

    # 生产者配置，发送消息的线程池的设置，选填
    producer:
      # 发送线程数，默认 3
      send-threads: 3
      # 默认每次发送时最大批量大小，默认 30
      send-batch: 30
      # 如果消息发送失败，重试次数，默认 10
      send-try-count: 10
      # 异步发送队列大小，默认 10000
      max-queue-size: 10000

    # 使用 QmqTemplate 发送消息的默认主题，默认值 default_subject
    template:
      default-subject: default_subject

    # 消费者配置，消费消息的线程池的设置，选填
    consumer:
      # 线程名称前缀，默认 qmq-process
      thread-name-prefix: qmq-process
      # 线程池大小，默认 2
      core-pool-size: 2
      # 最大线程池大小，默认 2
      max-pool-size: 2
      # 线程池队列大小，默认 1000
      queue-capacity: 1000

    # 消息主题和分组配置，选填
    # 使用 QmqConsumer 注解时，可使用 SpEL 表达式引入以下主题和分组
    subject:
      sub1: sub1
      sub2: sub2
      sub3: sub3
      # more subject ...
    group:
      group1: group1
      group2: group2
      group3: group3
      # more group ...

logging:
  level:
    # 设置 qmq-spring-boot-starter 的日志级别
    xin.wjtree.qmq: trace

server:
  port: 8989
```

## 发送消息

### 发送即时消息



### 发送延时消息

### 发送定时消息

## 消费消息

### 启用消费者模式

### 配置消费监听器