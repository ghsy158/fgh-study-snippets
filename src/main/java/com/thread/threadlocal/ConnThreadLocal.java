package com.thread.threadlocal;

/**
 * 
 * @author fgh
 * @Since 2016年3月13日 下午2:44:02
 */
public class ConnThreadLocal {

	public static ThreadLocal<String> tl = new ThreadLocal<String>();

	public void setTl(String value) {
		tl.set(value);
	}

	public void getTl() {
		System.out.println(Thread.currentThread().getName() + ":" + tl.get());
	}

	public static void main(String[] args) {
		final ConnThreadLocal ctl = new ConnThreadLocal();

		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				ctl.setTl("张三");
				ctl.getTl();
			}
		}, "t1");

		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					ctl.getTl();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "t2");

		t1.start();
		t2.start();
	}
}
