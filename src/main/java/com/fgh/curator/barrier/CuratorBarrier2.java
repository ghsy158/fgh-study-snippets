package com.fgh.curator.barrier;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;

/**
 * 分布式barrier，同时开始,不同时结束<br>
 * 正常使用时,把barrier放到公共的 类里,别的项目直接引用
 * 
 * @author fgh
 * @since 2016年7月23日上午10:49:54
 */
public class CuratorBarrier2 {

	private static final Logger logger = Logger.getLogger(CuratorBarrier2.class);

	private static final String CONNECT_ADDR = "localhost:2181";

	private static final int TIME_OUT = 10000;

	public static DistributedBarrier barrier = null;

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 5; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {

					try {
						RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
						CuratorFramework cf = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR)
								.sessionTimeoutMs(TIME_OUT).retryPolicy(retryPolicy).build();
						cf.start();

						barrier = new DistributedBarrier(cf, "/barrier");
						logger.info(Thread.currentThread().getName() + "设置barrier");
						barrier.setBarrier();// 设置
						barrier.waitOnBarrier();// 等待
						logger.info(Thread.currentThread().getName() + "开始执行程序....");
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}, "t" + i).start();
		}

		Thread.sleep(5000);
		
		barrier.removeBarrier();
	}
}
