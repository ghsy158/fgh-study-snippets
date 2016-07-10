package com.fgh.thread.singleton;

/**
 *  单例在多线程中最好用的，最安全的解决方案<br/>
 * @author fgh
 * @Since 2016年3月13日 下午2:53:23
 */
public class InnerSingleton {
	
	private static class Singleton{
		private  static Singleton single = new Singleton();
	}
	
	public static Singleton getInstance(){
		return Singleton.single;
	}
}
