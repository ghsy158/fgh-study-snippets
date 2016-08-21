package com.fgh.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import kafka.serializer.StringDecoder;
import kafka.serializer.StringEncoder;
import kafka.utils.VerifiableProperties;

/**
 * Kafaka Consumer
 * 
 * @author fgh
 * @since 2016年8月21日下午6:48:10
 */
public class KafakaConsumer {

	public static final String TOPIC = "test";

	public static void main(String[] args) throws InterruptedException {
		Properties conf  = new  Properties();
		conf.put("zookeeper.connect", "192.168.0.201:2181,192.168.0.202:2181,192.168.0.203:2181");
		conf.put("group.id", "gtoup1");
		conf.put("zookeeper.session.timeout.ms", "4000");
		conf.put("zookeeper.sync.interval.ms", "200");
		conf.put("auto.commit.interval.ms", "1000");
		conf.put("auto.offset.reset", "smallest");
		
		conf.put("serializer.class", StringEncoder.class.getName());
		ConsumerConfig config = new ConsumerConfig(conf);
		
		ConsumerConnector consumer = Consumer.createJavaConsumerConnector(config);
		Map<String,Integer> topicCountMap = new HashMap<String,Integer>();
		topicCountMap.put(TOPIC, 1);
		
		StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
		StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());
		
		
		Map<String,List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap,keyDecoder,valueDecoder);
		
		KafkaStream<String, String>  stream = consumerMap.get(TOPIC).get(0);
		
		ConsumerIterator<String, String>  it = stream.iterator();
	
		while(it.hasNext()){
			MessageAndMetadata<String, String>  data = it.next();
			String msg = data.message();
			System.out.println("msg:"+msg);
		}
	}
}
