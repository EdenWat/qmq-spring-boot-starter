package xin.wjtree.qmq.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import qunar.tc.qmq.MessageProducer;
import qunar.tc.qmq.MessageSendStateListener;
import qunar.tc.qmq.base.BaseMessage;
import xin.wjtree.qmq.constant.QmqTimeUnit;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author kye
 */
public class DefaultQmqSendBuilder implements QmqSendBuilder {
    private static final Logger log = LoggerFactory.getLogger(DefaultQmqSendBuilder.class);

    /**
     * 消息发送者
     */
    private MessageProducer producer;

    /**
     * 消息发送主题
     */
    private String subject;

    /**
     * 消息标签
     */
    private String tag;

    /**
     * 消息接收时间
     */
    private Date receiveTime;

    /**
     * 消息发送状态监听器，默认的回调方法仅打印发送结果日志
     */
    private MessageSendStateListener listener = new DefaultMessageSendStateListener();

    public DefaultQmqSendBuilder(MessageProducer producer, String subject) {
        this.producer = producer;
        this.subject = subject;
    }

    @Override
    public QmqSendBuilder tag(String tag) {
        if (StringUtils.hasText(tag)) {
            this.tag = tag;
        }
        return this;
    }

    @Override
    public QmqSendBuilder delay(Date date) {
        if (date != null && date.getTime() > System.currentTimeMillis()) {
            this.receiveTime = date;
        }
        return this;
    }

    @Override
    public QmqSendBuilder delay(LocalDateTime localDateTime) {
        if (localDateTime != null && localDateTime.isAfter(LocalDateTime.now())) {
            this.receiveTime = QmqUtil.localDateTimeToDate(localDateTime);
        }
        return this;
    }

    @Override
    public QmqSendBuilder delay(long duration, TimeUnit timeUnit) {
        if (duration > 0 && timeUnit != null) {
            long temp = System.currentTimeMillis() + timeUnit.toMillis(duration);
            this.receiveTime = new Date(temp);
        }
        return this;
    }

    @Override
    public QmqSendBuilder delay(QmqTimeUnit qmqTimeUnit) {
        if (qmqTimeUnit != null) {
            long temp = System.currentTimeMillis() + qmqTimeUnit.getTimeUnit().toMillis(qmqTimeUnit.getDuration());
            this.receiveTime = new Date(temp);
        }
        return this;
    }

    @Override
    public QmqSendBuilder listener(MessageSendStateListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
        return this;
    }

    @Override
    public void send(Object object) {
        if (ObjectUtils.isEmpty(object)) {
            throw new QmqException("QMQ 消息发送内容不能为空");
        }

        send(QmqUtil.beanToMap(object));
    }

    @Override
    public void send(Map<String, Object> content) {
        if (CollectionUtils.isEmpty(content)) {
            throw new QmqException("QMQ 消息发送内容不能为空");
        }

        // 装载消息对象
        BaseMessage message = (BaseMessage) producer.generateMessage(subject);
        // 装载消息标签
        if (StringUtils.hasText(tag)) {
            message.addTag(tag);
        }
        // 装载消息接收时间
        if (receiveTime != null) {
            message.setDelayTime(receiveTime);
        }
        // 遍历装载消息内容，过滤键值对为空的属性
        content.entrySet().stream().filter(e -> StringUtils.hasText(e.getKey()) && !ObjectUtils.isEmpty(e.getValue()))
                .forEach(entry -> bindMessage(message, entry));

        if (log.isTraceEnabled()) {
            log.trace("QMQ 异步消息准备发送，消息主题：{}，消息内容：{}", message.getSubject(), message.getAttrs());
        }
        // 发送消息，并返回回调结果
        producer.sendMessage(message, listener);
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
        } else if (value instanceof Byte) {
            message.setProperty(key, ((Byte) value).intValue());
        } else if (value instanceof Short) {
            message.setProperty(key, ((Short) value).intValue());
        } else if (value instanceof Integer) {
            message.setProperty(key, (Integer) value);
        } else if (value instanceof Long) {
            message.setProperty(key, (Long) value);
        } else if (value instanceof BigInteger) {
            message.setProperty(key, ((BigInteger) value).longValue());
        } else if (value instanceof Float) {
            message.setProperty(key, (Float) value);
        } else if (value instanceof Double) {
            message.setProperty(key, (Double) value);
        } else if (value instanceof BigDecimal) {
            message.setProperty(key, ((BigDecimal) value).doubleValue());
        } else if (value instanceof Date) {
            message.setProperty(key, (Date) value);
        } else if (value instanceof Character) {
            message.setProperty(key, ((Character) value).toString());
        } else if (value instanceof CharSequence) {
            String str = (String) value;
            // 判断字符串大小是否超过32K，使用 UTF-8 编码
            if (QmqUtil.isLargeString(str, StandardCharsets.UTF_8)) {
                message.setLargeString(key, str);
            } else {
                message.setProperty(key, str);
            }
        } else {
            throw new QmqException("Unexpected value: " + value.getClass());
        }
    }
}
