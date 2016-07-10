package com.thread.sync;

/**
 * 
 * @author fgh
 * @Since 2016年3月13日 下午6:37:25
 */
public class StringLock {

	public void method() {
		//new String("字符串常量")
		synchronized (new String("字符串常量")) {//string在常量池中只有一个引用 使用string常量枷锁，会出现死循环
			try {
				while (true) {
					System.out.println("当前线程：" + Thread.currentThread().getName() + "开始");
					Thread.sleep(1000);
					System.out.println("当前线程：" + Thread.currentThread().getName() + "结束");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		final StringLock stringLock = new StringLock();
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				stringLock.method();
			}
		},"t1");
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				stringLock.method();
			}
		},"t2");
		t1.start();
		t2.start();
	}
}
