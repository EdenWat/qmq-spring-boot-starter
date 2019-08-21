package xin.wjtree.qmq.internal;

import qunar.tc.qmq.MessageSendStateListener;
import xin.wjtree.qmq.constant.QmqTimeUnit;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author kye
 */
public interface QmqSendBuilder {
    /**
     * 设置消息发送主题的标签
     * @param tag 标签名称
     * @return {@link QmqSendBuilder}
     */
    QmqSendBuilder tag(String tag);

    /**
     * 设置消息接收时间
     * @param date 消息接收时间
     * @return {@link QmqSendBuilder}
     */
    QmqSendBuilder delay(Date date);

    /**
     * 设置消息接收时间
     * @param localDateTime 消息接收时间
     * @return {@link QmqSendBuilder}
     */
    QmqSendBuilder delay(LocalDateTime localDateTime);

    /**
     * 设置消息接收时间
     * @param duration 时间间隔
     * @param timeUnit 时间单位 {@link TimeUnit}
     * @return {@link QmqSendBuilder}
     */
    QmqSendBuilder delay(long duration, TimeUnit timeUnit);

    /**
     * 设置消息接收时间
     * @param qmqTimeUnit {@link QmqTimeUnit}
     * @return {@link QmqSendBuilder}
     */
    QmqSendBuilder delay(QmqTimeUnit qmqTimeUnit);

    /**
     * 设置自定义的消息发送状态监听器
     * @param listener 消息发送状态监听器
     * @return {@link QmqSendBuilder}
     */
    QmqSendBuilder listener(MessageSendStateListener listener);

    /**
     * 设置消息发送内容，支持回调方法，消息内容使用 Object
     * @param object 消息内容
     */
    void send(Object object);

    /**
     * 异步发送消息，支持回调方法，消息内容使用 Map
     * @param content 消息内容
     */
    void send(Map<String, Object> content);
}
