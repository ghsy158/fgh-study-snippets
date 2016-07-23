package com.fgh.curator.barrier;

import java.util.Random;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;

/**
 * 分布式barrier，同时开始,同时结束
 * @author fgh
 * @since 2016年7月23日上午10:49:54
 */
public class CuratorBarrier1 {

	private static final Logger logger = Logger.getLogger(CuratorBarrier1.class);

	private static final String CONNECT_ADDR = "localhost:2181";

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 5; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					
					try {
						RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
						CuratorFramework cf = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR)
								.retryPolicy(retryPolicy).build();
						cf.start();
						
						DistributedDoubleBarrier barrier = new DistributedDoubleBarrier(cf, "/barrier", 5);
						Thread.sleep(1000 * (new Random().nextInt(3)));
						logger.info(Thread.currentThread().getName()+"已经准备");
						barrier.enter();//准备await()
						logger.info("同时开始运行");
						Thread.sleep(1000 * (new Random().nextInt(3)));
						logger.info(Thread.currentThread().getName()+"运行完毕");
						barrier.leave();
						logger.info("同时退出运行");
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
					
				}
			},"t"+i).start();;
		}

	}
}
