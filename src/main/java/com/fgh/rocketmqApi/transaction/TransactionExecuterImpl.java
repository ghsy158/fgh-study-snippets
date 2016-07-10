package com.fgh.rocketmqApi.transaction;

import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.rocketmq.client.producer.LocalTransactionExecuter;
import com.alibaba.rocketmq.client.producer.LocalTransactionState;
import com.alibaba.rocketmq.common.message.Message;

/**
 * 执行本地事务，由客户端回调
 * @author fgh
 * @Since 2016年4月10日 下午8:57:34
 */
public class TransactionExecuterImpl implements LocalTransactionExecuter {

//	private AtomicInteger transactionIndex = new AtomicInteger(1);
	
	@Override
	public LocalTransactionState executeLocalTransactionBranch(Message msg, Object arg) {
		System.out.println("msg=="+new String(msg.getBody()));
		System.out.println("arg=="+arg);
		String tag = msg.getTags();
		if("Transaction1".equals(tag)){
			//这里有一个分阶段提交任务的概念
			System.out.println("这里处理业务逻辑，比如操作数据库，失败情况下进行rollback");
			return LocalTransactionState.ROLLBACK_MESSAGE;
		}
		return LocalTransactionState.COMMIT_MESSAGE;
	}

}
