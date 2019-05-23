package com.kyexpress.qmq;

import com.kyexpress.qmq.autoconfigure.QmqProperties;
import lombok.extern.slf4j.Slf4j;
import qunar.tc.qmq.Message;
import qunar.tc.qmq.MessageProducer;
import qunar.tc.qmq.MessageSendStateListener;

import java.util.Map;

/**
 * <ol>
 *    <li>QMQ的Message.setProperty(key, value)如果value是字符串，则value的大小默认不能超过32K，如果你需要传输超大的字符串，请务必使用message.setLargeString(key, value)，这样你甚至可以传输十几兆的内容了，但是消费消息的时候也需要使用message.getLargeString(key)。</li>
 * </ol>
 * @author kye
 */
@Slf4j
public class QmqTemplate {

	private MessageProducer producer;
	private QmqProperties properties;

	public QmqTemplate(MessageProducer producer, QmqProperties properties) {
		this.producer = producer;
		this.properties = properties;
	}

	public void send(Map<String, Object> content) {
		Message message = producer.generateMessage(properties.getDefaultSubject());

		for (Map.Entry<String, Object> entry : content.entrySet()) {
			message.setProperty(entry.getKey(), (Boolean) entry.getValue());
		}

		producer.sendMessage(message, new MessageSendStateListener() {
			@Override
			public void onSuccess(Message message) {
				//send success
				log.info("发送成功...");
			}

			@Override
			public void onFailed(Message message) {
				//send failed
				log.error("发送失败...");
			}
		});
	}

	public void send(Map<String, Object> content, String subject) {

	}

	public void sendDefault(Map<String, Object> content) {

	}
}