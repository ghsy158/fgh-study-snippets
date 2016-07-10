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
package com.fgh.rocketmqApi.order;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerOrderly;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * Consumer，订阅消息
 */
public class Consumer1 {

	public Consumer1() throws MQClientException {
		
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("order_consumer");

		consumer.setNamesrvAddr("192.168.1.201:9876;192.168.1.202:9876;192.168.1.203:9876;192.168.1.204:9876");

		/**
		 * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
		 * 如果非第一次启动，那么按照上次消费的位置继续消费
		 */
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

		// consumer.setConsumeMessageBatchMaxSize(10);

		consumer.subscribe("OrderTopic", "*");

		consumer.registerMessageListener(new Listener());

		consumer.start();

		System.out.println("Consumer1 Started.");
	}

	class Listener implements MessageListenerOrderly {

		private Random random = new Random();

		@Override
		public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
			context.setAutoCommit(true);
			for (MessageExt msg : msgs) {
				System.out.println(msg + ",content:" + new String(msg.getBody()));
			}

			try {
				// 模拟业务逻辑处理中
				TimeUnit.SECONDS.sleep(random.nextInt(2));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return ConsumeOrderlyStatus.SUCCESS;
		}
	}

	public static void main(String[] args) throws MQClientException {
		new Consumer1();
	}
}
