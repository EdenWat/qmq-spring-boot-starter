package xin.wjtree.qmq.internal;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DefaultMessageSendStateListener implements MessageSendStateListener {
	/**
	 * 消息发送成功时的回调方法
	 * @param message 消息对象
	 */
	@Override
	public void onSuccess(Message message) {
		log.info("QMQ 异步消息发送成功，消息主题：{}，消息内容：{}", message.getSubject(), ((BaseMessage) message).getAttrs());
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
