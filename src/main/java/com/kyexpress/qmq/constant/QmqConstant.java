package com.kyexpress.qmq.constant;

/**
 * QMQ 常量
 * @author Wang
 */
public final class QmqConstant {
	/**
	 * Qmq MetaServer 模板
	 */
	public static final String META_SERVER_TEMP = "http://%s:%d/meta/address";

	/**
	 * 公共属性：默认 MetaServer Host
	 */
	private static final String DEFAULT_HOST = "127.0.0.1";

	/**
	 * 公共属性：默认 MetaServer Port
	 */
	private static final int DEFAULT_PORT = 8080;

	/**
	 * 公共属性：默认 MetaServer
	 */
	public static final String DEFAULT_META_SERVER = String.format(META_SERVER_TEMP, DEFAULT_HOST, DEFAULT_PORT);

	/**
	 * 公共属性：默认 AppCode
	 */
	public static final String DEFAULT_APP_CODE = "default_app_code";

	/**
	 * 消息发送者属性：默认异步发送队列大小
	 */
	public static final int DEFAULT_MAX_QUEUE_SIZE = 10000;

	/**
	 * 消息发送者属性：默认发送线程数
	 */
	public static final int DEFAULT_SEND_THREADS = 3;

	/**
	 * 消息发送者属性：默认每次发送时最大批量大小
	 */
	public static final int DEFAULT_SEND_BATCH = 30;

	/**
	 * 消息发送者属性：默认消息发送失败时的重试次数
	 */
	public static final int DEFAULT_SEND_TRY_COUNT = 10;

	/**
	 * 消息发送者属性：默认发送消息超时时间，单位：毫秒
	 */
	public static final long DEFAULT_SEND_TIMEOUT_MILLIS = 5000L;

	/**
	 * 消息发送模板属性：默认消息发送主题
	 */
	public static final String DEFAULT_SUBJECT = "default_subject";

	/**
	 * 消息接收者属性：默认线程名称前缀
	 */
	public static final String DEFAULT_THREAD_NAME_PREFIX = "qmq-process";

	/**
	 * 消息接收者属性：默认线程池大小
	 */
	public static final int DEFAULT_CORE_POOL_SIZE = 2;

	/**
	 * 消息接收者属性：默认最大线程池大小
	 */
	public static final int DEFAULT_MAX_POOL_SIZE = 2;

	/**
	 * 消息接收者属性：默认线程队列大小
	 */
	public static final int DEFAULT_QUEUE_CAPACITY = 1000;

	/**
	 * 消息接收者属性：默认消费线程池名称
	 */
	public static final String DEFAULT_EXECUTOR_NAME = "qmqExecutor";
}
