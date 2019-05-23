package com.kyexpress.qmq.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author kye
 */
@Data
@ConfigurationProperties("spring.qmq")
public class QmqProperties {
	/**
	 * 是否启用 QMQ
	 * <p>default is false</p>
	 */
	private boolean enabled = false;

	/**
	 * QMQ AppCode
	 * <p>default is "qmq_default_code"</p>
	 */
	private String appCode = "qmq_default_code";

	/**
	 * QMQ MetaServer Address
	 * <p>default is false</p>
	 */
	private String metaServer = "http://127.0.0.1:8080/meta/address";

	/**
	 * 异步发送队列大小
	 */
	private int maxQueueSize = 10000;

	/**
	 * 发送线程数，默认是3
	 */
	private int sendThreads = 3;

	/**
	 * 默认每次发送时最大批量大小，默认30
	 */
	private int sendBatch = 30;

	/**
	 * 如果消息发送失败，重试次数，默认10
	 */
	private int sendTryCount = 10;

	/**
	 * 发送消息超时时间，单位：毫秒，默认 5 秒超时
	 * <p>源代码中没有 set 方法，未生效</p>
	 */
	@Deprecated
	private long sendTimeoutMillis = 5000L;

	/**
	 * 是否同步发送，默认使用异步发送
	 * <p>源代码中没有 set 方法，未生效</p>
	 */
	@Deprecated
	private boolean syncSend = false;

	/**
	 * 默认消息发送主题
	 * <p>default is "qmq_default_subject"</p>
	 */
	private String defaultSubject = "qmq_default_subject";
}