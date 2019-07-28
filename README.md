# 使用方式

![Maven Central](https://img.shields.io/maven-central/v/xin.wjtree.qmq/qmq-spring-boot-starter)
![GitHub release](https://img.shields.io/github/release/wjtree/qmq-spring-boot-starter)
![GitHub](https://img.shields.io/github/license/wjtree/qmq-spring-boot-starter)
![GitHub repo size](https://img.shields.io/github/repo-size/wjtree/qmq-spring-boot-starter)
![GitHub All Releases](https://img.shields.io/github/downloads/wjtree/qmq-spring-boot-starter/total)
![Gitter](https://img.shields.io/gitter/room/wjtree/qmq-spring-boot-starter)

- QMQ - https://github.com/qunarcorp/qmq
- Spring Boot Starter for QMQ - https://github.com/wjtree/qmq-spring-boot-starter

## 引入 Maven 依赖（已上传到中央仓库）

```xml
<dependency>
    <groupId>xin.wjtree.qmq</groupId>
    <artifactId>qmq-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 添加 Spring Boot 配置（YML）

```properties
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

```java
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import qunar.tc.qmq.Message;
import qunar.tc.qmq.MessageSendStateListener;
import xin.wjtree.qmq.QmqTemplate;
import xin.wjtree.qmq.autoconfigure.QmqProperties;
import xin.wjtree.qmq.constant.QmqTimeUnit;
import xin.wjtree.qmq.internal.QmqAlias;
import xin.wjtree.qmq.internal.QmqIgnore;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QmqTest {
    @Resource
    private QmqTemplate template;
    @Resource
    private QmqProperties properties;

    /**
     * 发送即时消息
     * @throws InterruptedException
     */
    @Test
    public void sendImmediate() throws InterruptedException {
        // 计数器，执行1次结束
        CountDownLatch latch = new CountDownLatch(1);

        // 一般使用 template.send(properties.getSubject().get("sub1"), getUser()) 即可
        template.withSendStateListener(new MessageSendStateListener() {
            @Override
            public void onSuccess(Message m) {
                latch.countDown();
            }

            @Override
            public void onFailed(Message m) {
                latch.countDown();
            }
        }).send(properties.getSubject().get("sub1"), getUser());

        // 计数器减1
        latch.await();
    }

    /**
     * 发送延时消息
     * @throws InterruptedException
     */
    @Test
    public void sendDelay() throws InterruptedException {
        // 计数器，执行1次结束
        CountDownLatch latch = new CountDownLatch(1);

        // 延时 10 秒发送消息
        // 一般使用 template.sendDelay(properties.getSubject().get("sub1"), getUser(), QmqTimeUnit.TEN_SECONDS) 即可
        template.withSendStateListener(new MessageSendStateListener() {
            @Override
            public void onSuccess(Message m) {
                latch.countDown();
            }

            @Override
            public void onFailed(Message m) {
                latch.countDown();
            }
        }).sendDelay(properties.getSubject().get("sub1"), getUser(), QmqTimeUnit.TEN_SECONDS);

        // 计数器减1
        latch.await();
    }

    /**
     * 发送定时消息
     * @throws InterruptedException
     */
    @Test
    public void sendSchedule() throws InterruptedException, ParseException {
        // 计数器，执行1次结束
        CountDownLatch latch = new CountDownLatch(1);

        // 定时发送的日期时间
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-07-28 00:16:00");

        // 一般使用 template.sendSchedule(properties.getSubject().get("sub1"), getUser(), date) 即可
        template.withSendStateListener(new MessageSendStateListener() {
            @Override
            public void onSuccess(Message m) {
                latch.countDown();
            }

            @Override
            public void onFailed(Message m) {
                latch.countDown();
            }
        }).sendSchedule(properties.getSubject().get("sub1"), getUser(), date);

        // 计数器减1
        latch.await();
    }

    public User getUser() {
        User user = new User();
        user.setId(100000000001L);
        user.setName("张三");
        user.setAge(120);
        user.setSchool("北京大学");
        user.setCompany("中石油");
        user.setDuty("行政总裁");
        user.setSalary(new BigDecimal("1000000"));
        user.setEnable(true);
        return user;
    }

    public static class User {
        @QmqAlias("user_id")
        private Long id;

        private String name;

        private Integer age;

        @QmqAlias("school_name")
        private String school;

        private String company;

        @QmqIgnore
        private String duty;

        private BigDecimal salary;

        private Boolean enable;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getDuty() {
            return duty;
        }

        public void setDuty(String duty) {
            this.duty = duty;
        }

        public BigDecimal getSalary() {
            return salary;
        }

        public void setSalary(BigDecimal salary) {
            this.salary = salary;
        }

        public Boolean getEnable() {
            return enable;
        }

        public void setEnable(Boolean enable) {
            this.enable = enable;
        }
    }
}
```

## 消费消息

### 启用消费者模式

- 在配置类上添加 EnableQmq 注解，包括 appCode 和 metaServer 属性

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import qunar.tc.qmq.consumer.annotation.EnableQmq;

@EnableQmq(appCode="${spring.qmq.app-code}", metaServer="${spring.qmq.meta-server}")
@SpringBootApplication
public class QmqApplication {

	public static void main(String[] args) {
		SpringApplication.run(QmqApplication.class, args);
	}

}
```

### 配置消费监听器

- 在方法上添加 QmqConsumer 注解，包括 subject，consumerGroup，executor 等属性
- executor = `QmqConstant.EXECUTOR_NAME` 表示消费线程池的 BeanName，该值固定为 `qmqExecutor`

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import qunar.tc.qmq.Message;
import qunar.tc.qmq.base.BaseMessage;
import qunar.tc.qmq.consumer.annotation.QmqConsumer;
import xin.wjtree.qmq.constant.QmqConstant;

@Slf4j
@Component
public class QmqLinstener {

    @QmqConsumer(subject = "${spring.qmq.subject.sub1}", consumerGroup = "${spring.qmq.group.group1}",
            executor = QmqConstant.EXECUTOR_NAME)
    public void onMessage(Message message) {
        log.info("qmq 消费主题：{}，消费消息：{}", message.getSubject(), ((BaseMessage) message).getAttrs());
    }

}
```