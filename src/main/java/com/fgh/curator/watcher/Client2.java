package com.fgh.curator.watcher;

/**
 * 
 * @author fgh
 * @since 2016年7月23日上午11:32:04
 */
public class Client2 {

	public static void main(String[] args) throws Exception {
		new CuratorWatcher();
		System.out.println("client2 start...");
		Thread.sleep(1000000000);
	}

}
