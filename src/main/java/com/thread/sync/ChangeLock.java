package com.thread.sync;

/**
 * 锁对象的改变问题
 * @author fgh
 * @Since 2016年3月13日 下午6:44:39
 */
public class ChangeLock {

	private String lock = "lock";
	
	private void method(){
		synchronized (new String("字符串常量")) {//string在常量池中只有一个引用 使用string常量枷锁，会出现死循环
			try {
				while (true) {
					System.out.println("当前线程：" + Thread.currentThread().getName() + "开始");
					//不要修改锁，已修改锁的对象就变化了，别的线程就可以进来了
					lock = "change lock";
					Thread.sleep(1000);
					System.out.println("当前线程：" + Thread.currentThread().getName() + "结束");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		final ChangeLock changeLock = new ChangeLock();
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				changeLock.method();
			}
		},"t1");
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				changeLock.method();
			}
		},"t2");
		t1.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		t2.start();
	}
}
