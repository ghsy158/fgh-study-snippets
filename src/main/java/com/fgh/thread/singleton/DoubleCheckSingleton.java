package com.fgh.thread.singleton;

/**
 * 单例 懒汉模式
 * @author fgh
 * @Since 2016年3月13日 下午2:52:57
 */
public class DoubleCheckSingleton {

	private static volatile DoubleCheckSingleton instance ;
	
	public static DoubleCheckSingleton getInstance(){
		if(null == instance){
			try {
				//模拟初始化对象的准备时间
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (DoubleCheckSingleton.class) {
				if(null == instance){
					instance = new DoubleCheckSingleton();
				}
			}
		}
		return instance;
	}
	
	public static void main(String[] args) {
		
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName()+":"+DoubleCheckSingleton.getInstance().hashCode());
				System.out.println(instance);
			}
		},"t1");
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName()+":"+DoubleCheckSingleton.getInstance().hashCode());
				System.out.println(instance);
			}
		},"t2");
		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName()+":"+DoubleCheckSingleton.getInstance().hashCode());
				System.out.println(instance);
			}
		},"t3");
		
		t1.start();
		t2.start();
		t3.start();
	}
}
