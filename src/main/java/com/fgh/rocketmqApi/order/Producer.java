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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

/**
 * Producer，发送消息
 * 
 */
public class Producer {
	public static void main(String[] args)
			throws MQClientException, InterruptedException, RemotingException, MQBrokerException {
		DefaultMQProducer producer = new DefaultMQProducer("order_consumer");
		producer.setNamesrvAddr("192.168.1.201:9876;192.168.1.202:9876;192.168.1.203:9876;192.168.1.204:9876");

		// producer.setRetryTimesWhenSendFailed(10);

		producer.start();

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(date);

		for (int i = 1; i <= 5; i++) {
			String body = ",我是订单" + i + " " + dateStr;
			Message msg = new Message("OrderTopic", "order_1", "Key" + i, body.getBytes());
			SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
				@Override
				public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
					Integer id = (Integer) arg;
					return mqs.get(id);
				}
			}, 0);
			System.out.println(sendResult + ",body:" + body);
		}
		
		for (int i = 1; i <= 5; i++) {
			String body = ",我是订单" + i + " " + dateStr;
			Message msg = new Message("OrderTopic", "order_2", "Key" + i, body.getBytes());
			SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
				@Override
				public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
					Integer id = (Integer) arg;
					return mqs.get(id);
				}
			}, 1);
			System.out.println(sendResult + ",body:" + body);
		}
		
		for (int i = 1; i <= 5; i++) {
			String body = ",我是订单" + i + " " + dateStr;
			Message msg = new Message("OrderTopic", "order_3", "Key" + i, body.getBytes());
			SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
				@Override
				public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
					Integer id = (Integer) arg;
					return mqs.get(id);
				}
			}, 2);
			System.out.println(sendResult + ",body:" + body);
		}

		producer.shutdown();
	}
}
