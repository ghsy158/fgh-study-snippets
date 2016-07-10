package com.thread.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UseCondition {

	private Lock lock = new ReentrantLock();
	private  Condition 	condition = lock.newCondition();
	
	public void method1(){
		try {
			lock.lock();
			System.out.println("当前线程："+Thread.currentThread().getName()+"进入等待状态...");
			Thread.sleep(3000);
			System.out.println("当前线程："+Thread.currentThread().getName()+"释放锁...");
			condition.await();//类似wait方法
			System.out.println("当前线程："+Thread.currentThread().getName()+"继续执行...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
	
	public void method2(){
		try {
			lock.lock();
			System.out.println("当前线程："+Thread.currentThread().getName()+"进入...");
			Thread.sleep(3000);
			System.out.println("当前线程："+Thread.currentThread().getName()+"发出唤醒...");
			condition.signal();//类似notify方法
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
	
	public static void main(String[] args) {
		final UseCondition condition = new UseCondition();
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				condition.method1();
			}
		},"t1");
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				condition.method2();
			}
		},"t2");
		t1.start();
		t2.start();
	}
}
