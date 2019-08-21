package xin.wjtree.qmq;

import org.springframework.util.StringUtils;
import qunar.tc.qmq.MessageProducer;
import xin.wjtree.qmq.autoconfigure.QmqProperties;
import xin.wjtree.qmq.internal.DefaultQmqSendBuilder;
import xin.wjtree.qmq.internal.QmqException;
import xin.wjtree.qmq.internal.QmqSendBuilder;

/**
 * QMQ 消息发送模板
 * @author Wang
 */
public class QmqTemplate {
    /**
     * 消息发送者
     */
    private final MessageProducer messageProducer;

    /**
     * 自动配置属性
     */
    private final QmqProperties qmqProperties;

    public QmqTemplate(MessageProducer messageProducer, QmqProperties qmqProperties) {
        this.messageProducer = messageProducer;
        this.qmqProperties = qmqProperties;
    }

    /**
     * 使用默认的主题发送消息
     * @return {@link QmqSendBuilder}
     * @see QmqProperties.Template#getDefaultSubject()
     */
    public QmqSendBuilder subject() {
        return new DefaultQmqSendBuilder(messageProducer, qmqProperties.getTemplate().getDefaultSubject());
    }

    /**
     * 设置消息发送主题
     * @param subject 主题名称
     * @return {@link QmqSendBuilder}
     */
    public QmqSendBuilder subject(String subject) {
        if (StringUtils.isEmpty(subject)) {
            throw new QmqException("消息发送主题不能为空");
        }

        // 尝试获取 spring.qmq.subject.[主题名称] 的键值对
        String propVal = qmqProperties.getSubject().get(subject);
        // 如果属性文件中有匹配的主题，则使用配置文件中的；否则直接使用入参名称作为主题名称
        return new DefaultQmqSendBuilder(messageProducer, StringUtils.hasText(propVal) ? propVal : subject);
    }

    public MessageProducer getMessageProducer() {
        return messageProducer;
    }

    public QmqProperties getQmqProperties() {
        return qmqProperties;
    }
}