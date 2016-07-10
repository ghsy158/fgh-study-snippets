package com.thread.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * 读写锁
 * @author fgh
 * @Since 2016年3月13日 下午9:03:58
 */
public class UseReenTrantReadWriteLock {

	private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	private ReadLock readLock = rwLock.readLock();
	private WriteLock writeLock = rwLock.writeLock();
	
	public void read(){
		try {
			readLock.lock();
			System.out.println("当前线程："+Thread.currentThread().getName()+"进入....");
			Thread.sleep(3000);
			System.out.println("当前线程："+Thread.currentThread().getName()+"退出....");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			readLock.unlock();
		}
	}
	
	public void write(){
		try {
			writeLock.lock();
			System.out.println("当前线程："+Thread.currentThread().getName()+"进入....");
			Thread.sleep(3000);
			System.out.println("当前线程："+Thread.currentThread().getName()+"退出....");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			writeLock.unlock();
		}
	}
	
	public static void main(String[] args) {
		final UseReenTrantReadWriteLock urrw = new UseReenTrantReadWriteLock();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				urrw.read();
			}
		}, "t1");
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				urrw.read();
			}
		}, "t2");
		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() {
				urrw.write();
			}
		}, "t3");
		Thread t4 = new Thread(new Runnable() {
			@Override
			public void run() {
				urrw.write();
			}
		}, "t4");
		
//		t1.start(); 两个线程读 可以并发的进入
//		t2.start();
		
//		t1.start();//R
		t3.start();//W
		t4.start();//W
	}
}
