package com.fgh.zookeeper.cluster;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
	
	private static final Logger logger = LoggerFactory.getLogger(Test.class);

	private static final String CONNECT_ADDR = "192.168.1.201:2181,192.168.1.202:2181,192.168.1.203:2181";
	static final int SESSION_TIMEOUT = 5000;

	private static final CountDownLatch connectedSemaphore = new CountDownLatch(1);

	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

		ZooKeeper zk = new ZooKeeper(CONNECT_ADDR, SESSION_TIMEOUT, new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				// 获取事件状态
				KeeperState keeperState = event.getState();
				// 事件类型
				EventType eventType = event.getType();
				// 如果是建立连接
				if (KeeperState.SyncConnected == keeperState) {
					if (EventType.None == eventType) {
						// 如果建立连接成功，则发送信号量,让后续阻塞程序向下执行
						connectedSemaphore.countDown();
						logger.info("zk 建立连接...");
					}
				}
			}
		});
		
		connectedSemaphore.await();
		zk.create("/super/a1", "a1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.create("/super/a2", "a2".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.create("/super/a3", "a3".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		zk.create("/super/a4", "a4".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
		zk.setData("/super/a1", "modify a1".getBytes(), -1);
		zk.setData("/super/a2", "modify a2".getBytes(), -1);
		byte[] data  = zk.getData("/super/a2", false, null);
		logger.info(new String(data));
		System.out.println(zk.exists("/super/a3", false));
		zk.delete("/super/a3", -1);
		
		zk.close();
	}
}
