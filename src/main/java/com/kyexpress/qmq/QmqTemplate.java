package com.kyexpress.qmq;

import com.google.gson.Gson;
import com.kyexpress.qmq.autoconfigure.QmqProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import qunar.tc.qmq.Message;
import qunar.tc.qmq.MessageProducer;
import qunar.tc.qmq.MessageSendStateListener;

import java.util.Date;
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
	 * 发送延迟消息到默认主题
	 * @param content 消息内容
	 * @param duration 延迟时间间隔
	 * @param timeUnit {@link TimeUnit} 延时时间单位
	 * @see com.kyexpress.qmq.constant.QmqConstant#DEFAULT_SUBJECT
	 */
	public void sendDelay(Map<String, Object> content, long duration, TimeUnit timeUnit) {
		sendDelay(properties.getDefaultSubject(), content, duration, timeUnit);
	}

	/**
	 * 发送延迟消息到指定主题
	 * @param subject 消息主题
	 * @param content 消息内容
	 * @param duration 延迟时间间隔
	 * @param timeUnit {@link TimeUnit} 延时时间单位
	 */
	public void sendDelay(String subject, Map<String, Object> content, long duration, TimeUnit timeUnit) {
		// 判断消息延迟发送时间
		Assert.isTrue(duration > 0, "消息延迟发送时间不能为过去时");
		Assert.notNull(timeUnit, "消息延迟发送时间单位不能为空");
		// 讲延迟时间转换为毫秒
		long sendTime = System.currentTimeMillis() + timeUnit.toMillis(duration);

		send(subject, content, new Date(sendTime));
	}

	/**
	 * 发送定时消息到默认主题
	 * @param content 消息内容
	 * @param date {@link Date} 消息发送日期，用于延迟或定时发送
	 * @see com.kyexpress.qmq.constant.QmqConstant#DEFAULT_SUBJECT
	 */
	public void sendDelay(Map<String, Object> content, Date date) {
		sendDelay(properties.getDefaultSubject(), content, date);
	}

	/**
	 * 发送定时消息到指定主题
	 * @param subject 消息主题
	 * @param content 消息内容
	 * @param date {@link Date} 消息发送日期，用于延迟或定时发送
	 */
	public void sendDelay(String subject, Map<String, Object> content, Date date) {
		// 判断消息定时发送时间
		Assert.notNull(date, "消息定时发送时间不能为空");
		Assert.isTrue(date.getTime() > System.currentTimeMillis(), "消息定时发送时间不能为过去时");

		send(subject, content, date);
	}

	/**
	 * 发送即时消息到默认主题
	 * @param content 消息内容
	 * @see com.kyexpress.qmq.constant.QmqConstant#DEFAULT_SUBJECT
	 */
	public void send(Map<String, Object> content) {
		send(properties.getDefaultSubject(), content);
	}

	/**
	 * 发送即时消息到指定主题
	 * @param subject 消息主题
	 * @param content 消息内容
	 */
	public void send(String subject, Map<String, Object> content) {
		send(subject, content, null);
	}

	/**
	 * 发送消息
	 * @param subject 消息主题
	 * @param content 消息内容
	 * @param date {@link Date} 消息发送日期，用于延迟或定时发送
	 */
	private void send(String subject, Map<String, Object> content, Date date) {
		// 转换消息对象
		Message message = convertMessage(subject, content);

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
	 * @return {@link Message} 消息实体
	 */
	private Message convertMessage(String subject, Map<String, Object> content) {
		// 判断消息内容
		Assert.notEmpty(content, "QMQ 消息发送内容 Content 不能为空");

		// init message
		Message message = initMessage(subject);
		Assert.notNull(message, "QMQ 消息发送对象 Message 不能为空");

		// 声明消息内容的键值对
		String key;
		Object value;

		// 遍历装载消息内容
		for (Map.Entry<String, Object> entry : content.entrySet()) {
			// 获取消息内容的键值对
			key = entry.getKey();
			value = entry.getValue();

			if (StringUtils.isBlank(key) || value == null) {
				log.warn("QMQ 消息内容的键值对为空，key：{}，value：{}", key, value);
				continue;
			}

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
				// TODO 判断字符串大小是否超过32K
				if (StringUtils.length(str) < 32) {
					message.setProperty(key, str);
				} else {
					message.setLargeString(key, str);
				}
			} else {
				throw new IllegalStateException("Unexpected value: " + value.getClass());
			}
		}

		return message;
	}

	/**
	 * 初始化消息实体
	 * @param subject 消息主题
	 * @return {@link Message} 消息实体
	 */
	private Message initMessage(String subject) {
		// 判断消息发送主题
		Assert.hasText(subject, "QMQ 消息发送主题 Subject 不能为空");

		// 传入消息发送主题，返回消息对象
		return producer.generateMessage(subject);
	}

	/**
	 * 异步发送消息，支持回调方法
	 * <p>QMQ 暂时不支持同步发送消息，如果需要同步发送，要通过修改源代码实现</p>
	 * @param message {@link Message} 消息实体
	 */
	private void sendMessage(Message message) {
		// 判断消息对象
		Assert.notNull(message, "QMQ 消息发送对象 Message 不能为空");

		// 发送消息，并返回回调结果
		producer.sendMessage(message, new MessageSendStateListener() {
			@Override
			public void onSuccess(Message message) {
				// send success
				log.info("QMQ 发送异步消息成功，消息主题：{}，消息内容：{}", message.getSubject(), new Gson().toJson(message));
			}

			@Override
			public void onFailed(Message message) {
				// send failed
				log.error("QMQ 发送异步消息失败，消息主题：{}，消息内容：{}", message.getSubject(), new Gson().toJson(message));
			}
		});
	}
}