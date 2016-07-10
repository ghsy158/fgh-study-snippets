package com.fgh.mq.p2p;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer {

	public final String SELECTOR_0 = "age > 25";
	public final String SELECTOR_1 = "color= 'blue'";
	public final String SELECTOR_2 = "color= 'blue' and sa1 > 3000";
	public final String SELECTOR_3 = "color= 'receiver='A'";
	
	ConnectionFactory factory = null;

	Connection conn = null;
	Session session = null;

	Destination destination = null;

	MessageConsumer consumer = null;
	
	public Consumer(){
		try {
			this.factory = new ActiveMQConnectionFactory("fgh", "fgh", "tcp://localhost:61616");
			this.conn = factory.createConnection();
			this.conn.start();
			this.session = this.conn.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			this.destination = this.session.createQueue("first");
			this.consumer = this.session.createConsumer(destination,SELECTOR_2);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public void receiver(){
		try {
			this.consumer.setMessageListener(new Listener());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Consumer consumer = new Consumer();
		consumer.receiver();
	}
}
