package xin.wjtree.qmq.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qunar.tc.qmq.Message;
import qunar.tc.qmq.MessageSendStateListener;
import qunar.tc.qmq.base.BaseMessage;

/**
 * 默认的消息发送状态监听器
 * <ul>
 *     <li>默认的回调方法仅打印发送结果日志</li>
 * </ul>
 * @author Wang
 */
public class DefaultMessageSendStateListener implements MessageSendStateListener {
    private static final Logger log = LoggerFactory.getLogger(DefaultMessageSendStateListener.class);

    /**
     * 消息发送成功时的回调方法
     * @param message 消息对象
     */
    @Override
    public void onSuccess(Message message) {
        if (log.isDebugEnabled()) {
            log.debug("QMQ 异步消息发送成功，消息主题：{}，消息内容：{}", message.getSubject(), ((BaseMessage) message).getAttrs());
        }
    }

    /**
     * 消息发送失败时的回调方法
     * @param message 消息对象
     */
    @Override
    public void onFailed(Message message) {
        log.error("QMQ 异步消息发送失败，消息主题：{}，消息内容：{}", message.getSubject(), ((BaseMessage) message).getAttrs());
    }
}
