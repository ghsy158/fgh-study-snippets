/**
 * Copyright (C) 2010-2013 Alibaba Group Holding Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fgh.rocketmqApi.quickstart;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * Consumer，订阅消息
 */
public class Consumer {

	public static void main(String[] args) throws InterruptedException, MQClientException {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("quickstart_consumer");
		/**
		 * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
		 * 如果非第一次启动，那么按照上次消费的位置继续消费
		 */
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

//		consumer.setConsumeMessageBatchMaxSize(10);
		
		consumer.subscribe("TopicQuickStart", "*");

		consumer.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//				System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs);
				System.out.println("消息条数："+msgs.size());
				MessageExt msg = msgs.get(0);
				try {
//					for (MessageExt msg : msgs) {
						String topic = msg.getTopic();
						String msgBody = new String(msg.getBody(), "utf-8");
						String tags = msg.getTags();
						System.out.println("收到消息： topic:"+topic+",tags:"+tags+",msg:"+msgBody);
						
						//一定要注意是先启动Consumer，在进行发送消息，（也就是先订阅服务，再发送）
//						if("Hello RocketMQ 4".equals(msgBody)){
//							System.out.println("========失败消息开始========");
//							System.out.println(msg);
//							System.out.println(msgBody);
//							System.out.println("========失败消息结束========");
//							
//							//异常
//							int a = 1/0;
//						}
//					}
				} catch (Exception e) {
					e.printStackTrace();
//					if(msg.getReconsumeTimes()==2){
//						//记录日志 ，持久化操作...
//						System.out.println("重试2次以后还是没有成功,记录日志，进行相关处理...");
//						return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//					}else{
//						return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//					}
						
				}

				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});

		consumer.setNamesrvAddr("192.168.1.201:9876;192.168.1.202:9876;192.168.1.203:9876;192.168.1.204:9876");
		consumer.start();

		System.out.println("Consumer Started.");
	}
}
