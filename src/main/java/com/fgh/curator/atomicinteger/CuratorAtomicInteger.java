package com.fgh.curator.atomicinteger;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.log4j.Logger;

/**
 * zookeeper分布式计数器
 * 
 * @author fgh
 * @since 2016年7月23日上午10:07:28
 */
public class CuratorAtomicInteger {

	private static final Logger logger = Logger.getLogger(CuratorAtomicInteger.class);
	
	private static final String CONNECT_ADDR = "localhost:2181";

	private static final int TIME_OUT = 15000;

	public static void main(String[] args) throws Exception {

		// 重试策略 初始时间1s 重试10次
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);

		CuratorFramework cf = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR).sessionTimeoutMs(TIME_OUT)
				.retryPolicy(retryPolicy).build();

		cf.start();

		DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(cf, "/atomicInteger",
				new RetryNTimes(3, 1000));

		atomicInteger.forceSet(0);
		
		AtomicValue<Integer> value  = atomicInteger.get();
		atomicInteger.increment();
		
		logger.info(value.succeeded());
		logger.info(value.postValue());//最新值
		logger.info(value.preValue());//原始值
	}
}
