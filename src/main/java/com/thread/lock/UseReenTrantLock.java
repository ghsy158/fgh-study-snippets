package com.thread.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁 必须要使用finally解锁
 * @author fgh
 * @Since 2016年3月13日 下午7:34:36
 */
public class UseReenTrantLock {

	private Lock lock = new ReentrantLock();
	
	public void method1(){
		try {
			lock.lock();
			System.out.println("当前线程："+Thread.currentThread().getName()+"进入method1...");
			Thread.sleep(1000);
			System.out.println("当前线程："+Thread.currentThread().getName()+"退出method1...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
	
	public void method2(){
		try {
			lock.lock();
			System.out.println("当前线程："+Thread.currentThread().getName()+"进入method2...");
			Thread.sleep(2000);
			System.out.println("当前线程："+Thread.currentThread().getName()+"退出method2...");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
	
	public static void main(String[] args) {
		final UseReenTrantLock utr = new UseReenTrantLock();
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				utr.method1();
				utr.method2();
			}
		},"t1");
		t1.start();
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
