package com.fgh.curator.watcher;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;

/**
 * CuratorWatcher客户端,测试两个客户端同时监听节点的变化
 * @author fgh
 * @since 2016年7月23日上午11:33:07
 */
public class CuratorWatcherTest {

	private static final Logger logger = Logger.getLogger(CuratorWatcherTest.class);

	private static final String CONNECT_ADDR = "localhost:2181";

	private static final int TIME_OUT = 10000;

	static final String PARENT_PATH="/watcher";
	
	public static void main(String[] args) throws Exception {

		// 1 重试策略 初始时间1s 重试10次
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);

		//2  创建curtor对象
		CuratorFramework cf = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR).sessionTimeoutMs(TIME_OUT)
				.retryPolicy(retryPolicy).build();

		//3建立连接
		cf.start();
		
		//添加节点
		Thread.sleep(1000);
		if (cf.checkExists().forPath("/watcher/c1") == null) {
			cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/watcher/c1","c1内容".getBytes());
		}

		
		Thread.sleep(1000);
		if (cf.checkExists().forPath("/watcher/c2") == null) {
			cf.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/watcher/c2","c2内容".getBytes());
		}

		//修改节点
		Thread.sleep(1000);
		cf.setData().forPath("/watcher/c1","修改的新的c1内容".getBytes());
		String ret = new String(cf.getData().forPath("/watcher/c1"));
		logger.info(ret);
		
		//删除节点
		Thread.sleep(1000);
		cf.delete().forPath("/watcher/c2");
		
		Thread.sleep(1000);
		
		cf.close();

	}
}
