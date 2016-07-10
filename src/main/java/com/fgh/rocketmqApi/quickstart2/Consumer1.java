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
package com.fgh.rocketmqApi.quickstart2;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * Consumer，订阅消息
 */
public class Consumer1 {

	public Consumer1() {

		try {
			DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("message_consumer");
			/**
			 * 设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
			 * 如果非第一次启动，那么按照上次消费的位置继续消费
			 */
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);

			consumer.setConsumeMessageBatchMaxSize(10);
			consumer.setNamesrvAddr("192.168.1.201:9876;192.168.1.202:9876");
			consumer.subscribe("Topic1", "Tag1 || Tag2 || Tag3");
			// 广播模式下要先启动Consumer
			 consumer.setMessageModel(MessageModel.BROADCASTING);
			consumer.registerMessageListener(new Listener1());
			consumer.start();
			System.out.println("Consumer1 Started.");
		} catch (MQClientException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws InterruptedException, MQClientException {
		new Consumer1();
	}
}
