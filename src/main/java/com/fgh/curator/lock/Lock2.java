package com.fgh.curator.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 使用zookeeper实现分布式锁
 * 
 * @author fgh
 * @since 2016年7月20日下午1:02:39
 */
public class Lock2 {

	private static final String CONNECT_ADDR = "localhost:2181";

	private static final int TIME_OUT = 10000;

	static ReentrantLock reentrantLock = new ReentrantLock();
	static int count = 10;

	public static CuratorFramework createCuratorFramework() {
		// 重试策略 初始时间1s 重试10次
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);

		CuratorFramework cf = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR).sessionTimeoutMs(TIME_OUT)
				.retryPolicy(retryPolicy).build();
		return cf;

	}

	public static void main(String[] args) throws InterruptedException {
		final CountDownLatch countDown = new CountDownLatch(1);
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					CuratorFramework cf = createCuratorFramework();
					cf.start();
					//zookeeper的分布式锁
//					final InterProcessMutex lock = new InterProcessMutex(cf, "/lock");
					
					final ReentrantLock lock = new ReentrantLock();
					try {
						countDown.await();
//						lock.acquire();
						lock.lock();
						System.out.println(Thread.currentThread().getName()+"执行业务逻辑...");
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
//							lock.release();
							lock.unlock();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}, "t" + i).start();
		}

		Thread.sleep(2000);
		countDown.countDown();
	}
}
