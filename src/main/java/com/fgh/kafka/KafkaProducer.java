package com.fgh.kafka;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;


/**
 * Kafka Producer
 * @author fgh
 * @since 2016年8月21日下午6:08:54
 */
public class KafkaProducer {

	public static final String 	TOPIC="test";
	@SuppressWarnings({ "deprecation", "unchecked" })
	public static void main(String[] args) throws InterruptedException {
		Properties conf  = new  Properties();
		conf.put("zookeeper.connect", "192.168.0.201:2181,192.168.0.202:2181,192.168.0.203:2181");
		conf.put("serializer.class", StringEncoder.class.getName());
		conf.put("metadata.broker.list", "192.168.0.201:9092");
		conf.put("request.required.acks", "1");
		
		Producer producer  = new Producer<Integer, String>(new ProducerConfig(conf));
		
		for(int i=0;i<10;i++){
			producer.send(new KeyedMessage<Integer, String>(TOPIC, "hello kafka "+ i));
			System.out.println("send message:"+"hello kafka "+ i);
			TimeUnit.SECONDS.sleep(1);
		}
		
		producer.close();
	
	
	}
}
