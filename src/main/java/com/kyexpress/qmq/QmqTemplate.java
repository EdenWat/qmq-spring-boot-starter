package com.kyexpress.qmq;

import com.kyexpress.qmq.autoconfigure.QmqProperties;
import com.kyexpress.qmq.constant.TimeUnitEnum;
import com.kyexpress.qmq.util.QmqUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import qunar.tc.qmq.Message;
import qunar.tc.qmq.MessageProducer;
import qunar.tc.qmq.MessageSendStateListener;
import qunar.tc.qmq.base.BaseMessage;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * QMQ 消息发送模板
 * @author kye
 */
@Slf4j
public class QmqTemplate {
	/**
	 * QMQ 消息发送者
	 */
	private MessageProducer producer;
	/**
	 * QMQ 自动配置属性
	 */
	private QmqProperties properties;

	/**
	 * Init QmqTemplate
	 * @param producer {@link MessageProducer}
	 * @param properties {@link QmqProperties}
	 */
	public QmqTemplate(MessageProducer producer, QmqProperties properties) {
		this.producer = producer;
		this.properties = properties;
	}

	/**
	 * 发送延迟消息到默认主题，消息内容使用 Map
	 * @param content 消息内容
	 * @param timeUnitEnum 延迟时间枚举
	 */
	public void sendDelayDefault(Map<String, Object> content, TimeUnitEnum timeUnitEnum) {
		sendDelay(properties.getTemplate().getDefaultSubject(), content, timeUnitEnum);
	}

	/**
	 * 发送延迟消息到默认主题，消息内容使用 Map
	 * @param content 消息内容
	 * @param duration 延迟时间间隔
	 * @param timeUnit 延时时间单位
	 * @see com.kyexpress.qmq.constant.QmqConstant#DEFAULT_SUBJECT
	 */
	public void sendDelayDefault(Map<String, Object> content, long duration, TimeUnit timeUnit) {
		sendDelay(properties.getTemplate().getDefaultSubject(), content, duration, timeUnit);
	}

	/**
	 * 发送定时消息到默认主题，消息内容使用 Map
	 * @param content 消息内容
	 * @param date {@link Date} 消息发送日期，用于延迟或定时发送
	 * @see com.kyexpress.qmq.constant.QmqConstant#DEFAULT_SUBJECT
	 */
	public void sendDelayDefault(Map<String, Object> content, Date date) {
		sendDelay(properties.getTemplate().getDefaultSubject(), content, date);
	}

	/**
	 * 发送即时消息到默认主题，消息内容使用 Map
	 * @param content 消息内容
	 * @see com.kyexpress.qmq.constant.QmqConstant#DEFAULT_SUBJECT
	 */
	public void sendDefault(Map<String, Object> content) {
		send(properties.getTemplate().getDefaultSubject(), content);
	}

	/**
	 * 发送延迟消息到指定主题，消息内容使用 Object
	 * @param subject 消息主题
	 * @param content 消息内容
	 * @param timeUnitEnum 延迟时间枚举
	 */
	public void sendDelay(String subject, Object content, TimeUnitEnum timeUnitEnum) {
		sendDelay(subject, content, timeUnitEnum.getDuration(), timeUnitEnum.getTimeUnit());
	}

	/**
	 * 发送延迟消息到指定主题，消息内容使用 Map
	 * @param subject 消息主题
	 * @param content 消息内容
	 * @param timeUnitEnum 延迟时间枚举
	 */
	public void sendDelay(String subject, Map<String, Object> content, TimeUnitEnum timeUnitEnum) {
		sendDelay(subject, content, timeUnitEnum.getDuration(), timeUnitEnum.getTimeUnit());
	}

	/**
	 * 发送延迟消息到指定主题，该方法仅在消息内容只有一个参数时使用
	 * @param subject 消息主题
	 * @param key 消息参数键
	 * @param value 消息参数值
	 * @param duration 延迟时间间隔
	 * @param timeUnit 延时时间单位
	 */
	public void sendDelay(String subject, String key, Object value, long duration, TimeUnit timeUnit) {
		// 将键值对转换为 Map
		Map<String, Object> content = new HashMap<>(1);
		content.put(key, value);

		sendDelay(subject, content, duration, timeUnit);
	}

	/**
	 * 发送延迟消息到指定主题，消息内容使用 Object
	 * @param subject 消息主题
	 * @param content 消息内容
	 * @param duration 延迟时间间隔
	 * @param timeUnit 延时时间单位
	 */
	public void sendDelay(String subject, Object content, long duration, TimeUnit timeUnit) {
		// 判断消息延迟发送时间
		Assert.isTrue(duration > 0, "消息延迟接收时间不能为过去时");
		Assert.notNull(timeUnit, "消息延迟接收时间单位不能为空");

		// 讲延迟时间转换为毫秒
		long sendTime = System.currentTimeMillis() + timeUnit.toMillis(duration);
		send(subject, content, new Date(sendTime));
	}

	/**
	 * 发送延迟消息到指定主题，消息内容使用 Map
	 * @param subject 消息主题
	 * @param content 消息内容
	 * @param duration 延迟时间间隔
	 * @param timeUnit 延时时间单位
	 */
	public void sendDelay(String subject, Map<String, Object> content, long duration, TimeUnit timeUnit) {
		// 判断消息延迟发送时间
		Assert.isTrue(duration > 0, "消息延迟接收时间不能为过去时");
		Assert.notNull(timeUnit, "消息延迟接收时间单位不能为空");

		// 讲延迟时间转换为毫秒
		long sendTime = System.currentTimeMillis() + timeUnit.toMillis(duration);
		send(subject, content, new Date(sendTime));
	}

	/**
	 * 发送定时消息到指定主题，该方法仅在消息内容只有一个参数时使用
	 * @param subject 消息主题
	 * @param key 消息参数键
	 * @param value 消息参数值
	 * @param date 消息发送日期，用于延迟或定时发送
	 */
	public void sendDelay(String subject, String key, Object value, Date date) {
		// 将键值对转换为 Map
		Map<String, Object> content = new HashMap<>(1);
		content.put(key, value);

		sendDelay(subject, content, date);
	}

	/**
	 * 发送定时消息到指定主题，消息内容使用 Object
	 * @param subject 消息主题
	 * @param content 消息内容
	 * @param date 消息发送日期，用于延迟或定时发送
	 */
	public void sendDelay(String subject, Object content, Date date) {
		// 判断消息定时发送时间
		Assert.notNull(date, "消息定时接收时间不能为空");
		Assert.isTrue(date.getTime() > System.currentTimeMillis(), "消息定时接收时间不能为过去时");

		send(subject, content, date);
	}

	/**
	 * 发送定时消息到指定主题，消息内容使用 Map
	 * @param subject 消息主题
	 * @param content 消息内容
	 * @param date 消息发送日期，用于延迟或定时发送
	 */
	public void sendDelay(String subject, Map<String, Object> content, Date date) {
		// 判断消息定时发送时间
		Assert.notNull(date, "消息定时接收时间不能为空");
		Assert.isTrue(date.getTime() > System.currentTimeMillis(), "消息定时接收时间不能为过去时");

		send(subject, content, date);
	}

	/**
	 * 发送即时消息到指定主题，消息内容使用 Object
	 * @param subject 消息主题
	 * @param content 消息内容
	 */
	public void send(String subject, Object content) {
		send(subject, content, null);
	}

	/**
	 * 发送即时消息到指定主题，消息内容使用 Map
	 * @param subject 消息主题
	 * @param content 消息内容
	 */
	public void send(String subject, Map<String, Object> content) {
		send(subject, content, null);
	}

	/**
	 * 发送消息，消息内容使用 Object
	 * @param subject 消息主题
	 * @param content 消息对象
	 * @param date 消息发送日期，用于延迟或定时发送
	 */
	private void send(String subject, Object content, Date date) {
		// 发送消息，将 Object 转换为 Map
		send(subject, QmqUtil.objToMap(content), date);
	}

	/**
	 * 发送消息，消息内容使用 Map
	 * @param subject 消息主题
	 * @param content 消息内容
	 * @param date 消息发送日期，用于延迟或定时发送
	 */
	private void send(String subject, Map<String, Object> content, Date date) {
		// 参数校验
		Assert.hasText(subject, "QMQ 消息发送主题 Subject 不能为空");
		Assert.notEmpty(content, "QMQ 消息发送内容 Content 不能为空");

		// 转换消息对象
		BaseMessage message = convertMessage(subject, content);
		// 装载延迟消息时间
		if (date != null && date.getTime() > System.currentTimeMillis()) {
			message.setDelayTime(date);
		}

		// 发送消息
		sendMessage(message);
	}

	/**
	 * 组装消息对象
	 * @param subject 消息主题
	 * @param content 消息内容
	 * @return {@link BaseMessage} 消息实体
	 */
	private BaseMessage convertMessage(String subject, Map<String, Object> content) {
		// init message
		BaseMessage message = (BaseMessage) producer.generateMessage(subject);
		Assert.notNull(message, "QMQ 消息发送对象 BaseMessage 不能为空");

		// 遍历装载消息内容
		for (Map.Entry<String, Object> entry : content.entrySet()) {
			// 键值对校验
			if (StringUtils.isBlank(entry.getKey()) || entry.getValue() == null) {
				log.warn("QMQ 消息内容的键值对为空，key：{}，value：{}", entry.getKey(), entry.getValue());
				continue;
			}
			// 根据数据类型，选择 set 方法
			bindMessage(message, entry);
		}

		return message;
	}

	/**
	 * 组装消息键值对
	 * <p>QMQ 目前支持的数据类型包含：</p>
	 * <ol>
	 *     <li>{@link Boolean}</li>
	 *     <li>{@link Integer}</li>
	 *     <li>{@link Long}</li>
	 *     <li>{@link Float}</li>
	 *     <li>{@link Double}</li>
	 *     <li>{@link Date}</li>
	 *     <li>{@link String}</li>
	 * </ol>
	 * @param message 消息对象
	 * @param entry 单条消息键值对
	 */
	private void bindMessage(BaseMessage message, Map.Entry<String, Object> entry) {
		// 声明消息内容的键值对
		String key = entry.getKey();
		Object value = entry.getValue();

		// 判断 value 的数据类型，并装载消息内容
		if (value instanceof Boolean) {
			message.setProperty(key, (Boolean) value);
		} else if (value instanceof Integer) {
			message.setProperty(key, (Integer) value);
		} else if (value instanceof Long) {
			message.setProperty(key, (Long) value);
		} else if (value instanceof Float) {
			message.setProperty(key, (Float) value);
		} else if (value instanceof Double) {
			message.setProperty(key, (Double) value);
		} else if (value instanceof Date) {
			message.setProperty(key, (Date) value);
		} else if (value instanceof String) {
			String str = (String) value;
			// 判断字符串大小是否超过32K，使用 UTF-8 编码
			if (QmqUtil.greaterThan32K(str, StandardCharsets.UTF_8)) {
				message.setLargeString(key, str);
			} else {
				message.setProperty(key, str);
			}
		} else {
			throw new IllegalStateException("Unexpected value: " + value.getClass());
		}
	}

	/**
	 * 异步发送消息，支持回调方法
	 * <ul>
	 *     <li>默认的毁掉方法仅打印发送结果日志</li>
	 *     <li>QMQ 暂时不支持同步发送消息，如果需要同步发送，要通过修改源代码实现</li>
	 * </ul>
	 * @param message {@link BaseMessage} 消息实体
	 */
	private void sendMessage(BaseMessage message) {
		if (log.isDebugEnabled()) {
			log.debug("QMQ 异步消息准备发送，消息主题：{}，消息内容：{}", message.getSubject(), message.getAttrs());
		}

		// TODO 需要支持自定义的回调，使用链式调用方法
		// 发送消息，并返回回调结果
		producer.sendMessage(message, new MessageSendStateListener() {
			@Override
			public void onSuccess(Message message) {
				// send success
				log.info("QMQ 异步消息发送成功，消息主题：{}，消息内容：{}", message.getSubject(), ((BaseMessage) message).getAttrs());
			}

			@Override
			public void onFailed(Message message) {
				// send failed
				log.error("QMQ 异步消息发送失败，消息主题：{}，消息内容：{}", message.getSubject(), ((BaseMessage) message).getAttrs());
			}
		});
	}
}