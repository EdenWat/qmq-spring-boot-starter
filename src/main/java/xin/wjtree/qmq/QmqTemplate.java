package xin.wjtree.qmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import qunar.tc.qmq.MessageProducer;
import qunar.tc.qmq.MessageSendStateListener;
import qunar.tc.qmq.base.BaseMessage;
import xin.wjtree.qmq.autoconfigure.QmqProperties;
import xin.wjtree.qmq.constant.QmqConstant;
import xin.wjtree.qmq.constant.TimeUnitEnum;
import xin.wjtree.qmq.internal.DefaultMessageSendStateListener;
import xin.wjtree.qmq.internal.QmqUtil;

import java.nio.charset.StandardCharsets;
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
	private final MessageProducer producer;
	/**
	 * QMQ 自动配置属性
	 */
	private final QmqProperties.Template properties;

	/**
	 * QMQ 消息发送状态监听器，默认的回调方法仅打印发送结果日志
	 */
	private MessageSendStateListener listener = new DefaultMessageSendStateListener();

	public QmqTemplate(MessageProducer producer, QmqProperties.Template properties) {
		this.producer = producer;
		this.properties = properties;
	}

	/**
	 * 设置自定义的消息发送状态监听器，使用链式调用方法
	 * @param listener 消息发送状态监听器
	 * @return {@link QmqTemplate}
	 */
	public QmqTemplate buildListener(MessageSendStateListener listener) {
		this.listener = listener;
		return this;
	}

	/**
	 * 发送延迟消息到默认主题，无标签，消息内容使用 Object
	 * @param object 消息对象
	 * @param duration 延迟时间间隔
	 * @param timeUnit 延时时间单位
	 * @see QmqConstant#DEFAULT_SUBJECT
	 */
	public void sendDelayDefault(Object object, long duration, TimeUnit timeUnit) {
		sendDelay(properties.getDefaultSubject(), object, duration, timeUnit);
	}

	/**
	 * 发送定时消息到默认主题，无标签，消息内容使用 Object
	 * @param object 消息对象
	 * @param date 消息发送日期，用于延迟或定时发送
	 * @see QmqConstant#DEFAULT_SUBJECT
	 */
	public void sendScheduleDefault(Object object, Date date) {
		sendSchedule(properties.getDefaultSubject(), object, date);
	}

	/**
	 * 发送即时消息到默认主题，无标签，消息内容使用 Object
	 * @param object 消息对象
	 * @see QmqConstant#DEFAULT_SUBJECT
	 */
	public void sendDefault(Object object) {
		send(properties.getDefaultSubject(), object);
	}

	/**
	 * 发送延迟消息到指定主题，无标签，消息内容使用 Object
	 * @param subject 消息主题
	 * @param object 消息对象
	 * @param timeUnitEnum {@link TimeUnitEnum} 时间单位枚举
	 */
	public void sendDelay(String subject, Object object, TimeUnitEnum timeUnitEnum) {
		sendDelay(subject, object, timeUnitEnum.getDuration(), timeUnitEnum.getTimeUnit());
	}

	/**
	 * 发送延迟消息到指定主题，无标签，消息内容使用 Object
	 * @param subject 消息主题
	 * @param object 消息对象
	 * @param duration 延迟时间间隔
	 * @param timeUnit 延时时间单位
	 */
	public void sendDelay(String subject, Object object, long duration, TimeUnit timeUnit) {
		sendDelay(subject, null, object, duration, timeUnit);
	}

	/**
	 * 发送延迟消息到指定主题，消息内容使用 Object
	 * @param subject 消息主题
	 * @param tag 消息标签
	 * @param object 消息对象
	 * @param duration 延迟时间间隔
	 * @param timeUnit 延时时间单位
	 */
	public void sendDelay(String subject, String tag, Object object, long duration, TimeUnit timeUnit) {
		sendDelay(subject, tag, QmqUtil.objToMap(object), duration, timeUnit);
	}

	/**
	 * 发送延迟消息到指定主题，消息内容使用 Map
	 * @param subject 消息主题
	 * @param tag 消息标签
	 * @param content 消息内容
	 * @param duration 延迟时间间隔
	 * @param timeUnit 延时时间单位
	 */
	public void sendDelay(String subject, String tag, Map<String, Object> content, long duration, TimeUnit timeUnit) {
		// 判断消息延迟发送时间
		Assert.isTrue(duration > 0, "消息延迟接收时间不能为过去时");
		Assert.notNull(timeUnit, "消息延迟接收时间单位不能为空");
		// 讲延迟时间转换为毫秒
		long sendTime = System.currentTimeMillis() + timeUnit.toMillis(duration);

		sendMessage(subject, tag, content, new Date(sendTime));
	}

	/**
	 * 发送定时消息到指定主题，无标签，消息内容使用 Object
	 * @param subject 消息主题
	 * @param object 消息对象
	 * @param date 消息发送日期，用于延迟或定时发送
	 */
	public void sendSchedule(String subject, Object object, Date date) {
		sendSchedule(subject, null, object, date);
	}

	/**
	 * 发送定时消息到指定主题，消息内容使用 Object
	 * @param subject 消息主题
	 * @param tag 消息标签
	 * @param object 消息对象
	 * @param date 消息发送日期，用于延迟或定时发送
	 */
	public void sendSchedule(String subject, String tag, Object object, Date date) {
		sendSchedule(subject, tag, QmqUtil.objToMap(object), date);
	}

	/**
	 * 发送定时消息到指定主题，消息内容使用 Map
	 * @param subject 消息主题
	 * @param tag 消息标签
	 * @param content 消息内容
	 * @param date 消息发送日期，用于延迟或定时发送
	 */
	public void sendSchedule(String subject, String tag, Map<String, Object> content, Date date) {
		// 判断消息定时发送时间
		Assert.notNull(date, "消息定时接收时间不能为空");
		Assert.isTrue(date.getTime() > System.currentTimeMillis(), "消息定时接收时间不能为过去时");

		sendMessage(subject, tag, content, date);
	}

	/**
	 * 发送即时消息到指定主题，无标签，消息内容使用 Object
	 * @param subject 消息主题
	 * @param object 消息对象
	 */
	public void send(String subject, Object object) {
		send(subject, null, object);
	}

	/**
	 * 发送即时消息到指定主题，消息内容使用 Object
	 * @param subject 消息主题
	 * @param tag 消息标签
	 * @param object 消息对象
	 */
	public void send(String subject, String tag, Object object) {
		send(subject, tag, QmqUtil.objToMap(object));
	}

	/**
	 * 发送即时消息到指定主题，消息内容使用 Map
	 * @param subject 消息主题
	 * @param tag 消息标签
	 * @param content 消息内容
	 */
	public void send(String subject, String tag, Map<String, Object> content) {
		sendMessage(subject, tag, content, null);
	}

	/**
	 * 异步发送消息，支持回调方法，消息内容使用 Map
	 * <ul>
	 *     <li>QMQ 暂时不支持同步发送消息，如果需要同步发送，要通过修改源代码实现</li>
	 * </ul>
	 * @param subject 消息主题
	 * @param tag 消息标签
	 * @param content 消息内容
	 * @param date 消息发送日期，用于延迟或定时发送
	 */
	private void sendMessage(String subject, String tag, Map<String, Object> content, Date date) {
		// 参数校验
		Assert.hasText(subject, "QMQ 消息发送主题 Subject 不能为空");
		Assert.notEmpty(content, "QMQ 消息发送内容 Content 不能为空");

		// 转换消息对象
		BaseMessage message = convertMessage(subject, content);
		// 装载延迟消息时间
		if (date != null && date.getTime() > System.currentTimeMillis()) {
			message.setDelayTime(date);
		}
		// 装载消息 Tag 标签
		if (StringUtils.hasText(tag)) {
			message.addTag(tag);
		}

		if (log.isDebugEnabled()) {
			log.debug("QMQ 异步消息准备发送，消息主题：{}，消息内容：{}", message.getSubject(), message.getAttrs());
		}
		// 发送消息，并返回回调结果
		producer.sendMessage(message, listener);
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
		content.entrySet().forEach(entry -> bindMessage(message, entry));

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
		// 键值对校验
		if (StringUtils.isEmpty(entry.getKey()) || entry.getValue() == null) {
			log.warn("QMQ 消息内容的键值对为空，key：{}，value：{}", entry.getKey(), entry.getValue());
			return;
		}

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
}