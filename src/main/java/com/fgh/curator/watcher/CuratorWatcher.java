package com.fgh.curator.watcher;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;

/**
 * 
 * @author fgh
 * @since 2016年7月23日上午11:20:49
 */
public class CuratorWatcher {

	private static final Logger logger = Logger.getLogger(CuratorWatcher.class);

	private static final String CONNECT_ADDR = "localhost:2181";

	private static final int TIME_OUT = 10000;

	static final String PARENT_PATH = "/watcher";

	public CuratorWatcher() throws Exception {
		// 1 重试策略 初始时间1s 重试10次
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);

		// 2 创建curtor对象
		CuratorFramework cf = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR).sessionTimeoutMs(TIME_OUT)
				.retryPolicy(retryPolicy).build();

		// 3建立连接
		cf.start();
		// 4创建根节点
		if (cf.checkExists().forPath(PARENT_PATH) == null) {
			cf.create().withMode(CreateMode.PERSISTENT).forPath(PARENT_PATH, "super init".getBytes());
		}

		// 5 建立一个PathChildrenCache缓存,第三个参数为是否接受节点数据内容,如果为false,不接受
		PathChildrenCache cache = new PathChildrenCache(cf, PARENT_PATH, true);
		// 6 在初始化时进行缓存监听
		cache.start(StartMode.POST_INITIALIZED_EVENT);
		cache.getListenable().addListener(new PathChildrenCacheListener() {

			/**
			 * 
			 * <b>方法描述：</b>监听子节点变更
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
					logger.info("添加节点：" + event.getData().getPath());
					logger.info("添加节点内容：" + new String(event.getData().getData()));
					break;
				case CHILD_REMOVED:
					logger.info("删除节点：" + event.getData().getPath());
					logger.info("删除节点内容：" + new String(event.getData().getData()));
					break;
				case CHILD_UPDATED:
					logger.info("更新节点：" + event.getData().getPath());
					logger.info("更新节点内容：" + new String(event.getData().getData()));
					break;
				default:
					break;
				}
			}
		});

	}
}
