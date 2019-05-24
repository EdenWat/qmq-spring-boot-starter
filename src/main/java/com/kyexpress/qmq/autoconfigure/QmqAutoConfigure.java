package com.kyexpress.qmq.autoconfigure;

import com.kyexpress.qmq.QmqTemplate;
import com.kyexpress.qmq.constant.QmqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
	@ConditionalOnMissingBean(MessageProducer.class)
	@ConditionalOnProperty(prefix = "spring.qmq", value = "enabled", havingValue = "true")
	public MessageProducer producer(QmqProperties properties) {
		MessageProducerProvider producer = new MessageProducerProvider();
		// appCode
		producer.setAppCode(StringUtils.defaultString(properties.getAppCode(), QmqConstant.DEFAULT_APP_CODE));
		// metaServer address
		producer.setMetaServer(StringUtils.defaultString(properties.getMetaServer(), QmqConstant.DEFAULT_META_SERVER));
		// 异步发送队列大小，默认10000
		producer.setMaxQueueSize(
				properties.getMaxQueueSize() > 0 ? properties.getMaxQueueSize() : QmqConstant.DEFAULT_MAX_QUEUE_SIZE);
		// 发送线程数，默认3
		producer.setSendThreads(
				properties.getSendThreads() > 0 ? properties.getSendThreads() : QmqConstant.DEFAULT_SEND_THREADS);
		// 默认每次发送时最大批量大小，默认30
		producer.setSendBatch(
				properties.getSendBatch() > 0 ? properties.getSendBatch() : QmqConstant.DEFAULT_SEND_BATCH);
		// 如果消息发送失败，重试次数，默认10
		producer.setSendTryCount(
				properties.getSendTryCount() > 0 ? properties.getSendTryCount() : QmqConstant.DEFAULT_SEND_TRY_COUNT);

		if (log.isDebugEnabled()) {
			log.debug("init qmq MessageProducer success");
		}

		return producer;
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
			log.debug("init qmq QmqTemplate success");
		}

		return new QmqTemplate(producer, properties);
	}
}