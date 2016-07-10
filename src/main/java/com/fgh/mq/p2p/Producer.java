package com.fgh.mq.p2p;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer {

	ConnectionFactory factory = null;

	Connection conn = null;
	Session session = null;
	// Session session = conn.createSession(Boolean.TRUE,
	// Session.CLIENT_ACKNOWLEDGE);

	Destination destination = null;

	MessageProducer producer = null;

	public Producer() {
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
	
	public Session getSession(){
		return this.session;
	}
	
	public void send1(){
		try {
			Destination destination = session.createQueue("first");
			MapMessage msg1 = this.session.createMapMessage();
			msg1.setString("name", "张三");
			msg1.setString("age", "23");
			msg1.setStringProperty("color", "blue");
			msg1.setIntProperty("sa1", 2200);
//			int id =1;
//			msg1.setInt("id", id);
//			
//			String receiver = id %2==0?"A":"B";
//			msg1.setStringProperty("receiver", receiver);
			
			MapMessage msg2 = this.session.createMapMessage();
			msg2.setString("name", "李四");
			msg2.setString("age", "29");
			msg2.setStringProperty("color", "red");
			msg2.setIntProperty("sa1", 9200);
//			
//			 id =2;
//			msg2.setInt("id", id);
//			
//			receiver = id %2==0?"A":"B";
//			msg2.setStringProperty("receiver", receiver);
			
			MapMessage msg3 = this.session.createMapMessage();
			msg3.setString("name", "王五");
			msg3.setString("age", "39");
			msg3.setStringProperty("color", "greeen");
			msg3.setIntProperty("sa1", 8200);
//			
//			 id =3;
//			msg3.setInt("id", id);
//			
//			receiver = id %2==0?"A":"B";
//			msg3.setStringProperty("receiver", receiver);
			MapMessage msg4= this.session.createMapMessage();
			msg4.setString("name", "赵六");
			msg4.setString("age", "25");
			msg4.setStringProperty("color", "blue");
			msg4.setIntProperty("sa1", 4800);
//			
//			 id =3;
//			msg4.setInt("id", id);
//			
//			receiver = id %2==0?"A":"B";
//			msg4.setStringProperty("receiver", receiver);
			
			
			this.producer.send(destination, msg1, DeliveryMode.PERSISTENT,2, 1000*60*10L);
			this.producer.send(destination, msg2, DeliveryMode.PERSISTENT,3, 1000*60*10L);
			this.producer.send(destination, msg3, DeliveryMode.PERSISTENT,6, 1000*60*10L);
			this.producer.send(destination, msg4, DeliveryMode.PERSISTENT,9, 1000*60*10L);
			
			System.out.println("消息发送完毕");
			this.conn.close();
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	
	
	public void send2(){	
		try {
			Destination destination = this.session.createQueue("first");
			TextMessage msg = this.session.createTextMessage();
			this.producer.send(destination, msg, DeliveryMode.NON_PERSISTENT,9, 1000*60*10L);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		Producer producer = new Producer();
		producer.send1();
	}
	
	
	
	
	
	
	
	
	
	
}
