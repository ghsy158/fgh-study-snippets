package com.thread.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多Condition
 * 
 * @author fgh
 * @Since 2016年3月13日 下午8:03:57
 */
public class UseManyCondition {
	private Lock lock = new ReentrantLock();
	private Condition condition1 = lock.newCondition();
	private Condition condition2 = lock.newCondition();

	public void method1() {
		try {
			lock.lock();
			System.out.println("当前线程：" + Thread.currentThread().getName() + "进入method1等待...");
			condition1.await();
			System.out.println("当前线程：" + Thread.currentThread().getName() + "method1继续...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void method2() {
		try {
			lock.lock();
			System.out.println("当前线程：" + Thread.currentThread().getName() + "进入method2等待...");
			condition1.await();
			System.out.println("当前线程：" + Thread.currentThread().getName() + "method2继续...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	public void method3() {
		try {
			lock.lock();
			System.out.println("当前线程：" + Thread.currentThread().getName() + "进入method3等待...");
			condition2.await();
			System.out.println("当前线程：" + Thread.currentThread().getName() + "method3继续...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	public void method4() {
		try {
			lock.lock();
			System.out.println("当前线程：" + Thread.currentThread().getName() + "唤醒...");
			condition1.signalAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public void method5() {
		try {
			lock.lock();
			System.out.println("当前线程：" + Thread.currentThread().getName() + "唤醒...");
			condition2.signalAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		final UseManyCondition umc = new UseManyCondition();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				umc.method1();
			}
		}, "t1");
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				umc.method2();
			}
		}, "t2");
		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() {
				umc.method3();
			}
		}, "t3");
		Thread t4 = new Thread(new Runnable() {
			@Override
			public void run() {
				umc.method4();
			}
		}, "t4");
		Thread t5 = new Thread(new Runnable() {
			@Override
			public void run() {
				umc.method5();
			}
		}, "t5");
		t1.start();
		t2.start();
		t3.start();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t4.start();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t5.start();
	}

}
