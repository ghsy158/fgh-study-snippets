package com.fgh.curator.watcher;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 这种方式用的不多
 * @author fgh
 * @since 2016年7月13日下午10:14:31
 */
public class CuratorWatcher1 {
	private static final String CONNECT_ADDR = "192.168.1.201:2181,192.168.1.202:2181,192.168.1.203:2181";

	private static final int TIME_OUT = 10000;

	public static void main(String[] args) throws Exception {

		// 重试策略 初始时间1s 重试10次
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);

		CuratorFramework cf = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR).sessionTimeoutMs(TIME_OUT)
				.retryPolicy(retryPolicy).build();

		cf.start();
		
		//建立cache缓存
		final NodeCache cache = new NodeCache(cf, "/super",false);
		
		cache.start(true);
		
		cache.getListenable().addListener(new NodeCacheListener() {
			
			/**
			 * 
			 * <b>方法描述：</b>触发事件为创建节点和更新节点，在删除节点的时候并不触发此操作<br>
			 * @param @throws Exception
			 */
			@Override
			public void nodeChanged() throws Exception {
				System.out.println("路径为:"+cache.getCurrentData().getPath());
				System.out.println("数据为:"+new String(cache.getCurrentData().getData()));
				System.out.println("状态为:"+cache.getCurrentData().getStat());
				System.out.println("-----------------------------------------");
			}
		});
		
		Thread.sleep(1000);
		
		cf.create().forPath("/super","123".getBytes());
		
		Thread.sleep(1000);
		
		cf.setData().forPath("/super","456".getBytes());
		
		Thread.sleep(Integer.MAX_VALUE);
		
	}
}
