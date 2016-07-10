package com.fgh.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Receiver {

	public static void main(String[] args) throws JMSException {

		ConnectionFactory factory = new ActiveMQConnectionFactory("fgh", "fgh", "tcp://0.0.0.0:61616");

		Connection conn = factory.createConnection();
		conn.start();

		Session session = conn.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);

		Destination destination = session.createQueue("queue1");

		MessageConsumer consumer = session.createConsumer(destination);

		while (true) {

			TextMessage textMessage = (TextMessage) consumer.receive();
			if (textMessage == null) {
				break;
			}
			System.out.println("收到的内容:" + textMessage.getText());
			//手动签收消息，另起一个线程(TCP)，通知MQ服务器，确认签收
			textMessage.acknowledge();
		}

		if (null != conn) {
			conn.close();
		}

		System.out.println("生产者生产消息完毕");

	}
}
