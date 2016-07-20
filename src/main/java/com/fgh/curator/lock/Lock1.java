package com.fgh.curator.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 单个JVM下可以保证同步，分布式（多JVM）下不能保证
 * @author fgh
 * @since 2016年7月20日下午1:02:52
 */
public class Lock1 {

	static ReentrantLock reentrantLock = new ReentrantLock();
	static int count=10;
	
	public static void generateNo(){
		try {
			reentrantLock.lock();
			count--;
			System.out.println("count="+count);
		} finally{
			reentrantLock.unlock();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		final CountDownLatch countDown= new CountDownLatch(1);
		for(int i=0;i<10;i++){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						countDown.await();
						generateNo();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally{
						
					}
				}
			},"t"+i).start();
		}
		
		Thread.sleep(1000);
		countDown.countDown();
	}
}
