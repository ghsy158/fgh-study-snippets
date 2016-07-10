package com.thread;

/**
 * 对象锁的同步和异步问题
 * 
 * @author fgh
 * @Since 2016年3月13日 下午4:20:29
 */
public class MyObject {

	public synchronized void method1() {

		try {
			System.out.println(Thread.currentThread().getName());
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//synchronized
	public synchronized void method2() {
		System.out.println(Thread.currentThread().getName());
	}

	public static void main(String[] args) {
		final MyObject obj = new MyObject();

		// 分析
		// t1线程先持有Object对象的Lock锁，t2线程可以以异步的方式调用对象中的非synchronized修饰的方法
		// t1线程先持有object对象的Lock锁，t2线程如果在这个时候调用对象中的同步方法，则需要等待，也就是同步

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				obj.method1();
			}
		}, "t1");

		Thread t2 = new Thread(new Runnable() {
			public void run() {
				obj.method2();
			}
		}, "t2");

		t1.start();
		t2.start();
	}

}
