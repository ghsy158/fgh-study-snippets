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
package com.fgh.rocketmqApi.transaction;

import java.util.concurrent.TimeUnit;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.LocalTransactionState;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.TransactionCheckListener;
import com.alibaba.rocketmq.client.producer.TransactionMQProducer;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * Producer，发送消息
 * 
 */
public class Producer {
	public static void main(String[] args) throws MQClientException, InterruptedException {
		
		/**
		 * 一个应用创建一个producer，由应用来维护此对象，可以设置为全局对象或者单例
		 * 注意：ProducerGroupName需要由应用来保证唯一，一类Producer集合的名称，这类Producer通常发送一类消息，且发送逻辑一致
		 * ProducerGroup这个概念发送普通对象时，作用不大，但是发送分布式消息时，比较关键
		 * 因为服务器会回查这个Group下的任意一个Producer
		 */
		final TransactionMQProducer producer = new TransactionMQProducer("transaction_producer");
		
		producer.setNamesrvAddr("192.168.1.201:9876;192.168.1.202:9876;192.168.1.203:9876;192.168.1.204:9876");
		
		//事务回查最小并发数
		producer.setCheckThreadPoolMinSize(5);
		//事务回查最大并发数
		producer.setCheckThreadPoolMaxSize(20);
		
		//队列数
		producer.setCheckRequestHoldMax(2000);
		
		//Produce对象在使用之前必须要调用start初始化，初始化一次即可
		//注意:切记不可以在每次发送消息时，都调用start方法
		producer.start();

		//服务器回调Producer，检查本地事务分支成功还是失败
		producer.setTransactionCheckListener(new TransactionCheckListener() {
			
			// msg key
			//查询数据库 是否执行成功
			@Override
			public LocalTransactionState checkLocalTransactionState(MessageExt msg) {
				System.out.println("state----"+new String(msg.getBody()));
				return LocalTransactionState.COMMIT_MESSAGE;
//				return LocalTransactionState.ROLLBACK_MESSAGE;
//				return LocalTransactionState.UNKNOW;
			}
		});

		/**
		 * 下面这段代码表明一个producer对象可以发送多个topic，多个tag的消息。
		 * 注意：send方法是同步调用，只要不抛异常就标识成功，但发送成功也可会有多种状态，
		 * 例如消息写入Master成功，但是Slave不成功，这种情况消息属于成功，但是对于个别应用如果对消息可靠性要求极高
		 * 需要对这种情况做处理，另外，消息可能会存在发送失败的情况，失败重试由应用来处理
		 */
		TransactionExecuterImpl transExecuterImpl = new TransactionExecuterImpl();
		
		for(int i=1;i<=2;i++){
			Message msg = new Message("TopicTransaction","Transaction"+i,"key",("Hello Rocket"+i).getBytes());
			SendResult sendResult = producer.sendMessageInTransaction(msg, transExecuterImpl, "tq");
			System.out.println(sendResult);
			TimeUnit.SECONDS.sleep(1);
		}
		
		
		
		
		producer.shutdown();
	}
}
