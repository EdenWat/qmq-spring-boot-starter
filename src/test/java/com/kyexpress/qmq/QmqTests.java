package com.kyexpress.qmq;

import org.junit.Before;
import org.junit.Test;
import qunar.tc.qmq.Message;
import qunar.tc.qmq.MessageSendStateListener;
import qunar.tc.qmq.producer.MessageProducerProvider;

import java.util.concurrent.CountDownLatch;

public class QmqTests {

	private MessageProducerProvider producer;

	@Before
	public void init() {
		producer = new MessageProducerProvider();
		producer.init();
	}

	@Test
	public void test_qmq_send_message() {
		Message message = producer.generateMessage("your subject");
		message.setProperty("key", "value");

		CountDownLatch latch = new CountDownLatch(1);
		producer.sendMessage(message, new MessageSendStateListener() {
			@Override
			public void onSuccess(Message m) {
				latch.countDown();
			}

			@Override
			public void onFailed(Message m) {
				latch.countDown();
			}
		});

		try {
			latch.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
