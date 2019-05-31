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
	 * IPv4 地址正则表达式，示例：127.0.0.1
	 */
	public static final String IP4_REX = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

	/**
	 * IPv6 地址正则表达式
	 */
	public static final String IP6_REX = "^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$";

	/**
	 * 端口号正则表达式，示例：8080
	 */
	public static final String PORT_REX = "^(1(02[4-9]|0[3-9][0-9]|[1-9][0-9]{2})|[2-9][0-9]{3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";

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
	public static final String DEFAULT_APP_CODE = "qmq_default_code";

	/**
	 * 公共属性：默认消息发送主题
	 */
	public static final String DEFAULT_SUBJECT = "qmq_default_subject";

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
	 * 消息接收者属性：默认县城次
	 */
	public static final int DEFAULT_CORE_POOL_SIZE = 2;

	/**
	 * 消息接收者属性：
	 */
	public static final int DEFAULT_MAX_POOL_SIZE = 2;

	/**
	 * 消息接收者属性：
	 */
	public static final int DEFAULT_QUEUE_CAPACITY = 1000;

	/**
	 * 消息接收者属性：
	 */
	public static final String DEFAULT_THREAD_NAME_PREFIX = "qmq-process";
}
