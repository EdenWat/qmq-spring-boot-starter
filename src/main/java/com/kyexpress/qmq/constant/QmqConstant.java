package com.kyexpress.qmq.constant;

/**
 * QMQ 常量
 * @author Wang
 */
public final class QmqConstant {
	/**
	 * 默认 AppCode
	 */
	public static final String DEFAULT_APP_CODE = "qmq_default_code";

	/**
	 * 默认 MetaServer
	 */
	public static final String DEFAULT_META_SERVER = "http://127.0.0.1:8080/meta/address";

	/**
	 * 默认异步发送队列大小
	 */
	public static final int DEFAULT_MAX_QUEUE_SIZE = 10000;

	/**
	 * 默认发送线程数
	 */
	public static final int DEFAULT_SEND_THREADS = 3;

	/**
	 * 默认每次发送时最大批量大小
	 */
	public static final int DEFAULT_SEND_BATCH = 30;

	/**
	 * 默认消息发送失败时的重试次数
	 */
	public static final int DEFAULT_SEND_TRY_COUNT = 10;

	/**
	 * 默认消息发送主题
	 */
	public static final String DEFAULT_SUBJECT = "qmq_default_subject";
}
