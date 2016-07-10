package com.fgh.mq.cluster;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {

	public static void main(String[] args) throws JMSException, InterruptedException {

		ConnectionFactory factory = new ActiveMQConnectionFactory(
				ActiveMQConnectionFactory.DEFAULT_USER,
				ActiveMQConnectionFactory.DEFAULT_PASSWORD,
				"failover:(tcp://192.168.1.201:51511,tcp://192.168.1.201:51512,tcp://192.168.1.201:51513)?Randomize=false");

		Connection conn = factory.createConnection();
		conn.start();

		Session session = conn.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
		// Session session = conn.createSession(Boolean.TRUE,
		// Session.CLIENT_ACKNOWLEDGE);

		Destination destination = session.createQueue("clusterqueue");

		MessageProducer producer = session.createProducer(null);
		// producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		for (int i = 1; i <= 500000; i++) {
			TextMessage textMessage = session.createTextMessage();
			textMessage.setText("消息内容,id=" + i);
			// producer.send(textMessage);
			// 1、目的地
			// 2、消息
			// 3、是否持久化
			// 4、优先级(0-9 0-4为普通消息 5-9为加急 默认为4)
			// 5、消息在MQ中的有效期
			producer.send(destination, textMessage, DeliveryMode.NON_PERSISTENT, i, 1000L * 60);
			System.out.println("生产消息" + i);
			Thread.sleep(100);
		}

		// session.commit();

		if (null != conn) {
			conn.close();
		}

		System.out.println("生产者生产消息完毕");

	}
}
