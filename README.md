# qmq-spring-boot-starter

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