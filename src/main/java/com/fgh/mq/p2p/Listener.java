package com.fgh.mq.p2p;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class Listener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		System.out.println("接收到消息..");
		if (message instanceof TextMessage) {

		}
		if (message instanceof MapMessage) {
			MapMessage mapMessage = (MapMessage) message;
			System.out.println(mapMessage.toString());
			try {
				System.out.println(mapMessage.getString("name"));
				System.out.println(mapMessage.getString("age"));
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
