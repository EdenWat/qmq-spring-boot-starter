package xin.wjtree.qmq.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;
import qunar.tc.qmq.MessageConsumer;
import qunar.tc.qmq.MessageProducer;
import qunar.tc.qmq.producer.MessageProducerProvider;
import xin.wjtree.qmq.QmqTemplate;
import xin.wjtree.qmq.constant.QmqHelper;

/**
 * @author Wang
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.qmq", name = "meta-server")
@EnableConfigurationProperties(QmqProperties.class)
public class QmqAutoConfigure {
    private static final Logger log = LoggerFactory.getLogger(QmqAutoConfigure.class);

    @Bean
    @ConditionalOnMissingBean(MessageProducer.class)
    public MessageProducer messageProducer(QmqProperties properties) {
        // 获取消息生产者配置
        QmqProperties.Producer prop = properties.getProducer();

        // 实例化消息生产者对象
        MessageProducerProvider producer = new MessageProducerProvider();
        // appCode & metaServer address
        producer.setAppCode(properties.getAppCode());
        producer.setMetaServer(properties.getMetaServer());
        // 异步发送队列大小，默认10000
        producer.setMaxQueueSize(prop.getMaxQueueSize());
        // 发送线程数，默认3
        producer.setSendThreads(prop.getSendThreads());
        // 默认每次发送时最大批量大小，默认30
        producer.setSendBatch(prop.getSendBatch());
        // 如果消息发送失败，重试次数，默认10
        producer.setSendTryCount(prop.getSendTryCount());

        if (log.isDebugEnabled()) {
            log.debug("Init MessageProducer Success, appCode: {}, metaServer: {}, maxQueueSize: {}, "
                            + "sendThreads: {}, sendBatch: {}, sendTryCount: {}", properties.getAppCode(),
                    properties.getMetaServer(), prop.getMaxQueueSize(), prop.getSendThreads(), prop.getSendBatch(),
                    prop.getSendTryCount());
        }
        return producer;
    }

    @Bean
    @ConditionalOnMissingBean(QmqTemplate.class)
    @ConditionalOnBean(MessageProducer.class)
    public QmqTemplate qmqTemplate(MessageProducer producer, QmqProperties properties) {
        if (log.isDebugEnabled()) {
            log.debug("Init QmqTemplate Success, defaultSubject: {}", properties.getTemplate().getDefaultSubject());
        }
        return new QmqTemplate(producer, properties);
    }

    @Bean(QmqHelper.EXECUTOR_NAME)
    @ConditionalOnMissingBean(name = QmqHelper.EXECUTOR_NAME)
    @ConditionalOnBean(MessageConsumer.class)
    public ThreadPoolExecutorFactoryBean threadPoolExecutorFactoryBean(QmqProperties properties) {
        // 获取消息接收者配置
        QmqProperties.Consumer prop = properties.getConsumer();

        // 设置消费者线程池
        ThreadPoolExecutorFactoryBean bean = new ThreadPoolExecutorFactoryBean();
        bean.setCorePoolSize(prop.getCorePoolSize());
        bean.setMaxPoolSize(prop.getMaxPoolSize());
        bean.setQueueCapacity(prop.getQueueCapacity());
        bean.setThreadNamePrefix(prop.getThreadNamePrefix());

        if (log.isDebugEnabled()) {
            log.debug("Init Executor Success, corePoolSize: {}, maxPoolSize: {}, queueCapacity: {}, "
                            + "threadNamePrefix: {}", prop.getCorePoolSize(), prop.getMaxPoolSize(), prop.getQueueCapacity(),
                    prop.getThreadNamePrefix());
        }
        return bean;
    }
}