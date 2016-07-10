package com.fgh.mq.topic;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.memory.list.MessageList;

public class Consumer3 {

	ConnectionFactory factory = null;

	Connection conn = null;
	Session session = null;

	Destination destination = null;

	MessageConsumer consumer = null;
	
	public Consumer3(){
		try {
			this.factory = new ActiveMQConnectionFactory("fgh", "fgh", "tcp://localhost:61616");
			this.conn = factory.createConnection();
			this.conn.start();
			System.out.println("订阅者c3已启动...");
			this.session = this.conn.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	public void receive() throws JMSException{
		Destination destination = session.createTopic("topic1");
		consumer = session.createConsumer(destination);
		consumer.setMessageListener(new Listener());
	}
	
	class Listener implements MessageListener{

		@Override
		public void onMessage(Message msg) {
			if(msg instanceof TextMessage){
				System.out.println("c3 收到消息:-----------------");
				TextMessage textMsg = (TextMessage) msg;
				try {
					System.out.println(textMsg.getText());
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static void main(String[] args) throws JMSException {
		Consumer3 consumer = new Consumer3();
		consumer.receive();
	}
}
