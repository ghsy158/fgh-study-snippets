package com.fgh.curator.base;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class CuratorBase {

	private static final String CONNECT_ADDR = "192.168.1.201:2181,192.168.1.202:2181,192.168.1.203:2181";

	private static final int TIME_OUT = 10000;

	public static void main(String[] args) throws Exception {

		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);

		CuratorFramework cf = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR).sessionTimeoutMs(TIME_OUT)
				.retryPolicy(retryPolicy).build();
		
		cf.start();
		
//		cf.delete().deletingChildrenIfNeeded().forPath("/curator");

//		cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/curator/c1").valueOf("哈哈");
//		cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/curator/c2").valueOf("34");
		
//		cf.setData().forPath("/curator/c1","3423".getBytes());
		
		
		ExecutorService pool = Executors.newCachedThreadPool();
		
		cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
		.inBackground(new BackgroundCallback() {
			
			@Override
			public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
				System.out.println("code："+event.getResultCode());
				System.out.println("type："+event.getType());
				System.out.println("线程为："+Thread.currentThread().getName());
			}
		},pool)
		.forPath("/curator/c2","c2内容".getBytes());
		
		System.out.println("主线程:"+Thread.currentThread().getName());
		
		Thread.sleep(Integer.MAX_VALUE);
		
		cf.close();
	
	}
}
