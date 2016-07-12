package com.fgh.zkclient.watcher;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

/**
 * 
 * @author fgh
 * @since 2016年7月12日下午1:06:58
 */
public class ZkClientWatcher1 {
	private static final String CONNECT_ADDR = "localhost:2181";
	// private static final String CONNECT_ADDR =
	// "192.168.1.201:2181,192.168.1.202:2181,192.168.1.203:2181";

	public static void main(String[] args) throws InterruptedException {
		ZkClient zkc = new ZkClient(new ZkConnection(CONNECT_ADDR), 5000);
		zkc.subscribeChildChanges("/zkclient", new IZkChildListener() {

			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				System.out.println("parentPath:" + parentPath);
				System.out.println("currentChilds:" + currentChilds);
			}
		});

		zkc.subscribeDataChanges("/zkclient", new IZkDataListener() {

			@Override
			public void handleDataDeleted(String dataPath) throws Exception {
			}

			@Override
			public void handleDataChange(String dataPath, Object data) throws Exception {
			}
		});
		Thread.sleep(2000);

		zkc.deleteRecursive("/zkclient");
		Thread.sleep(1000);

		zkc.createPersistent("/zkclient");
		Thread.sleep(1000);
		zkc.createPersistent("/zkclient/c1", "c1内容");
		Thread.sleep(1000);
		zkc.createPersistent("/zkclient/c2", "c2内容");
		Thread.sleep(1000);
		zkc.createPersistent("/zkclient/c3", "c3内容");
		Thread.sleep(1000);
		zkc.writeData("/zkclient/c1", "c1新内容");
		zkc.delete("/zkclient/c2");
		Thread.sleep(1000);
		zkc.deleteRecursive("/zkclient");

		Thread.sleep(1000);
		zkc.close();
	}

}
