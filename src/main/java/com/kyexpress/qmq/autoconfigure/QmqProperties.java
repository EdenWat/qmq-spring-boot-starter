package com.kyexpress.qmq.autoconfigure;

import com.kyexpress.qmq.constant.QmqConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kye
 */
@Data
@ConfigurationProperties("spring.qmq")
public class QmqProperties {
	/**
	 * 是否开启 QMQ，默认值 false
	 */
	private boolean enabled = false;

	/**
	 * QMQ AppCode，默认值 qmq_default_code
	 */
	private String appCode = QmqConstant.DEFAULT_APP_CODE;

	/**
	 * QMQ MetaServer Address，默认值 http://127.0.0.1:8080/meta/address
	 */
	private String metaServer = QmqConstant.DEFAULT_META_SERVER;

	/**
	 * 异步发送队列大小，默认 10000
	 */
	private int maxQueueSize = QmqConstant.DEFAULT_MAX_QUEUE_SIZE;

	/**
	 * 发送线程数，默认 3
	 */
	private int sendThreads = QmqConstant.DEFAULT_SEND_THREADS;

	/**
	 * 默认每次发送时最大批量大小，默认 30
	 */
	private int sendBatch = QmqConstant.DEFAULT_SEND_BATCH;

	/**
	 * 如果消息发送失败，重试次数，默认 10
	 */
	private int sendTryCount = QmqConstant.DEFAULT_SEND_TRY_COUNT;

	/**
	 * 发送消息超时时间，单位：毫秒，默认 5 秒超时；
	 * 源代码中没有 set 方法，未生效
	 */
	@Deprecated
	private long sendTimeoutMillis = 5000L;

	/**
	 * 是否同步发送，默认使用异步发送；
	 * 源代码中没有 set 方法，未生效
	 */
	@Deprecated
	private boolean syncSend = false;

	/**
	 * 默认消息发送主题，默认值 qmq_default_subject
	 */
	private String defaultSubject = QmqConstant.DEFAULT_SUBJECT;
}