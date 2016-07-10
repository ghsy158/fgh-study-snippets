package com.fgh.mq.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Publish {

	ConnectionFactory factory = null;

	Connection conn = null;
	Session session = null;

	MessageProducer producer = null;

	public Publish() {
		try {
			this.factory = new ActiveMQConnectionFactory("fgh", "fgh", "tcp://localhost:61616");
			this.conn = factory.createConnection();
			this.conn.start();
			this.session = this.conn.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			this.producer = session.createProducer(null);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage() throws JMSException{
		Destination destination = session.createTopic("topic1");
		TextMessage textMessage = session.createTextMessage("我是内容");
		producer.send(destination, textMessage);
		System.out.println("消息发布完毕");
	}
	
	
	public static void main(String[] args) throws JMSException {
		Publish publish = new Publish();
		publish.sendMessage();
	}
}
