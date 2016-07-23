package com.fgh.curator.watcher;

/**
 * 
 * @author fgh
 * @since 2016年7月23日上午11:32:09
 */
public class Client1 {

	public static void main(String[] args) throws Exception {
		new CuratorWatcher();
		System.out.println("client1 start...");
		Thread.sleep(1000000000);
	}
	
}
