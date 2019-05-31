package com.kyexpress.qmq.autoconfigure;

import com.kyexpress.qmq.QmqTemplate;
import com.kyexpress.qmq.constant.QmqConstant;
import com.kyexpress.qmq.util.QmqUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;
import qunar.tc.qmq.MessageConsumer;
import qunar.tc.qmq.MessageProducer;
import qunar.tc.qmq.consumer.MessageConsumerProvider;
import qunar.tc.qmq.producer.MessageProducerProvider;

/**
 * @author kye
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "spring.qmq", value = "enabled", havingValue = "true")
@EnableConfigurationProperties(QmqProperties.class)
public class QmqAutoConfigure {
	/**
	 * Init MessageProducer
	 * @param properties {@link QmqProperties}
	 * @return {@link MessageProducerProvider}
	 */
	@Bean
	@ConditionalOnMissingBean(MessageProducer.class)
	public MessageProducer producer(QmqProperties properties) {
		// 获取消息发送者配置
		QmqProperties.Producer prop = properties.getProducer();

		MessageProducerProvider producer = new MessageProducerProvider();
		// appCode
		producer.setAppCode(properties.getAppCode());
		// metaServer address
		producer.setMetaServer(QmqUtil.defaultMetaServer(properties));
		// 异步发送队列大小，默认10000
		producer.setMaxQueueSize(prop.getMaxQueueSize());
		// 发送线程数，默认3
		producer.setSendThreads(prop.getSendThreads());
		// 默认每次发送时最大批量大小，默认30
		producer.setSendBatch(prop.getSendBatch());
		// 如果消息发送失败，重试次数，默认10
		producer.setSendTryCount(prop.getSendTryCount());

		if (log.isDebugEnabled()) {
			log.debug(
					"Init MessageProducer Success, maxQueueSize: {}, sendThreads: {}, sendBatch: {}, sendTryCount: {}",
					prop.getMaxQueueSize(), prop.getSendThreads(), prop.getSendBatch(), prop.getSendTryCount());
		}

		return producer;
	}

	/**
	 * Init MessageConsumer
	 * @param properties {@link QmqProperties}
	 * @return {@link MessageConsumerProvider}
	 */
	@Bean
	@ConditionalOnMissingBean(MessageConsumer.class)
	public MessageConsumer consumer(QmqProperties properties) {
		MessageConsumerProvider consumer = new MessageConsumerProvider();
		// appCode
		consumer.setAppCode(properties.getAppCode());
		// metaServer address
		consumer.setMetaServer(QmqUtil.defaultMetaServer(properties));
		// init MessageConsumer
		consumer.init();

		if (log.isDebugEnabled()) {
			log.debug("Init MessageConsumer Success");
		}

		return consumer;
	}

	/**
	 * Init QmqTemplate
	 * @param producer {@link MessageProducer}
	 * @param properties {@link QmqProperties}
	 * @return {@link QmqTemplate}
	 */
	@Bean
	@ConditionalOnMissingBean(QmqTemplate.class)
	@ConditionalOnBean(MessageProducer.class)
	public QmqTemplate template(MessageProducer producer, QmqProperties properties) {
		if (log.isDebugEnabled()) {
			log.debug("Init QmqTemplate Success");
		}

		return new QmqTemplate(producer, properties);
	}

	@Bean(name = QmqConstant.DEFAULT_EXECUTOR_NAME)
	@ConditionalOnMissingBean(name = QmqConstant.DEFAULT_EXECUTOR_NAME)
	@ConditionalOnBean(MessageConsumer.class)
	public ThreadPoolExecutorFactoryBean executor(QmqProperties properties) {
		// 获取消息接收者配置
		QmqProperties.Consumer prop = properties.getConsumer();

		// 设置消费者线程池
		ThreadPoolExecutorFactoryBean bean = new ThreadPoolExecutorFactoryBean();
		bean.setCorePoolSize(prop.getCorePoolSize());
		bean.setMaxPoolSize(prop.getMaxPoolSize());
		bean.setQueueCapacity(prop.getQueueCapacity());
		bean.setThreadNamePrefix(prop.getThreadNamePrefix());

		if (log.isDebugEnabled()) {
			log.debug(
					"Init Consumer Executor Success, corePoolSize: {}, maxPoolSize: {}, queueCapacity: {}, threadNamePrefix: {}",
					prop.getCorePoolSize(), prop.getMaxPoolSize(), prop.getQueueCapacity(), prop.getThreadNamePrefix());
		}

		return bean;
	}
}