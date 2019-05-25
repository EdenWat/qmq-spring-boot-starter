# qmq-spring-boot-starter

## Maven 依赖
```
<dependency>
    <groupId>com.kyexpress.qmq</groupId>
    <artifactId>qmq-spring-boot-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## Spring Boot 配置（YML）
```
spring:
  application:
    name: qmq
  qmq:
    enabled: true
    host: 127.0.0.1
    port: 8080
    app-code: qmq_local_home
    producer:
      send-try-count: 3
      send-threads: 5
    template:
      default-subject: qmq_local_home_subject

logging:
  level:
    com.kyexpress.qmq: debug
```