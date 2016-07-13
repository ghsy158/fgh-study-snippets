package com.fgh.curator.watcher;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 这种方式用的不多
 * 
 * @author fgh
 * @since 2016年7月13日下午10:14:31
 */
public class CuratorWatcher2 {
	private static final String CONNECT_ADDR = "192.168.1.201:2181,192.168.1.202:2181,192.168.1.203:2181";

	private static final int TIME_OUT = 10000;

	public static void main(String[] args) throws Exception {

		// 重试策略 初始时间1s 重试10次
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);

		CuratorFramework cf = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR).sessionTimeoutMs(TIME_OUT)
				.retryPolicy(retryPolicy).build();

		cf.start();

		// 4 建立cache缓存 第三个参数为是否接收节点数据变化内容
		PathChildrenCache cache = new PathChildrenCache(cf, "/super", true);

		// 5 在初始化的时候 就进行缓存监听
		cache.start(StartMode.POST_INITIALIZED_EVENT);

		cache.getListenable().addListener(new PathChildrenCacheListener() {

			/**
			 * 
			 * <b>方法描述：</b>监听子节点变更，新建、修改、删除
			 * 
			 * @param @param
			 *            client
			 * @param @param
			 *            event
			 * @param @throws
			 *            Exception
			 */
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				switch (event.getType()) {
				case CHILD_ADDED:
					System.out.println("CHILD_ADDED：" + event.getData().getPath());
					break;
				case CHILD_REMOVED:
					System.out.println("CHILD_REMOVED：" + event.getData().getPath());
					break;
				case CHILD_UPDATED:
					System.out.println("CHILD_UPDATED：" + event.getData().getPath());
					break;
				default:
					break;
				}
			}
		});

		Thread.sleep(1000);

		//创建本身节点 不发生变化
		cf.create().forPath("/super", "init".getBytes());

		Thread.sleep(1000);

		cf.setData().forPath("/super", "456".getBytes());

		Thread.sleep(Integer.MAX_VALUE);

	}
}
