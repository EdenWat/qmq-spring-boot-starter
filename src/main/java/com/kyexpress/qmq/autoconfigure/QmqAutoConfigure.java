package com.kyexpress.qmq.autoconfigure;

import com.kyexpress.qmq.QmqTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import qunar.tc.qmq.MessageProducer;
import qunar.tc.qmq.producer.MessageProducerProvider;

/**
 * @author kye
 */
@Slf4j
@Configuration
@ConditionalOnClass(MessageProducer.class)
@EnableConfigurationProperties(QmqProperties.class)
public class QmqAutoConfigure {
	/**
	 * Init MessageProducer
	 * @param properties {@link QmqProperties}
	 * @return {@link MessageProducerProvider}
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "spring.qmq", value = "enabled", havingValue = "true")
	public MessageProducer producer(QmqProperties properties) {
		MessageProducerProvider producer = new MessageProducerProvider();
		// appCode
		producer.setAppCode(properties.getAppCode());
		// metaServer address
		producer.setMetaServer(properties.getMetaServer());
		// 异步发送队列大小
		producer.setMaxQueueSize(properties.getMaxQueueSize());
		// 发送线程数，默认是3
		producer.setSendThreads(properties.getSendThreads());
		// 默认每次发送时最大批量大小，默认30
		producer.setSendBatch(properties.getSendBatch());
		// 如果消息发送失败，重试次数，默认10
		producer.setSendTryCount(properties.getSendTryCount());

		return producer;
	}

	/**
	 * Init QmqTemplate
	 * @param producer {@link MessageProducer}
	 * @param properties {@link QmqProperties}
	 * @return {@link QmqTemplate}
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(MessageProducer.class)
	public QmqTemplate template(MessageProducer producer, QmqProperties properties) {
		return new QmqTemplate(producer, properties);
	}
}