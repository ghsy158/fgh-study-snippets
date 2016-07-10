package com.fgh.rocketmqApi.quickstart2;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;

public class Listener implements MessageListenerConcurrently {

	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		System.out.println("消息条数：" + msgs.size());
		MessageExt msg = msgs.get(0);
		try {
			// for (MessageExt msg : msgs) {
			String topic = msg.getTopic();
			String msgBody = new String(msg.getBody(), "utf-8");
			String tags = msg.getTags();
			System.out.println("收到消息： topic:" + topic + ",tags:" + tags + ",msg:" + msgBody);

			// 一定要注意是先启动Consumer，在进行发送消息，（也就是先订阅服务，再发送）
//			if ("Hello RocketMQ 4".equals(msgBody)) {
//				System.out.println("========失败消息开始========");
//				System.out.println(msg);
//				System.out.println(msgBody);
//				System.out.println("========失败消息结束========");
//
//				// 异常
////				int a = 1 / 0;
//			}
			// }
			myProcess();
		} catch (Exception e) {
			e.printStackTrace();
//			if (msg.getReconsumeTimes() == 2) {
//				// 记录日志 ，持久化操作...
//				System.out.println("重试2次以后还是没有成功,记录日志，进行相关处理...");
//				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//			} else {
//				return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//			}
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;

		}

		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

	public void myProcess() {
		
	}
}
