package com.fgh.zookeeper.cluster;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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

import com.fgh.zookeeper.auth.ZookeeperAuth;

public class ZKWatcher implements Watcher {

	private static final Logger logger = LoggerFactory.getLogger(ZKWatcher.class);

	 ZooKeeper zk = null;

	private static final String PARENT_PATH = "/super";

	private static final CountDownLatch connectedSemaphore = new CountDownLatch(1);

	private List<String> cowaList = new CopyOnWriteArrayList<String>();

	private static final String CONNECT_ADDR = "192.168.1.201:2181,192.168.1.202:2181,192.168.1.203:2181";
	static final int SESSION_TIMEOUT = 5000;

	public ZKWatcher() {
		logger.info("创建zk连接,connectAddr[" + CONNECT_ADDR + "],sessionTimeout[" + SESSION_TIMEOUT + "]");
		try {
			zk = new ZooKeeper(CONNECT_ADDR, SESSION_TIMEOUT, this);
			logger.info("开始连接zk服务器...");
			connectedSemaphore.await();
		} catch (Exception e) {
			logger.info("创建zk连接失败,connectAddr[" + CONNECT_ADDR + "],sessionTimeout[" + SESSION_TIMEOUT + "]", e);
		}
	}

	@Override
	public void process(WatchedEvent event) {
		logger.info("进入process....event[" + event + "]");
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (null == event) {
			return;
		}

		KeeperState keeperState = event.getState();

		EventType eventType = event.getType();

		// 受影响的path
		String path = event.getPath();
		logger.info("受影响的path:" + path);

		try {
			if (KeeperState.SyncConnected == keeperState) {
				// 成功连接上zk服务器
				if (EventType.None == eventType) {
					logger.info("成功连接上zk服务器");
					connectedSemaphore.countDown();
					if (this.zk.exists(PARENT_PATH, false) == null) {
						this.zk.create(PARENT_PATH, "root".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					}
					List<String> paths = this.zk.getChildren(PARENT_PATH, true);
					for (String p : paths) {
						logger.info(p);
						this.zk.exists(PARENT_PATH + "/" + p, true);
					}
				} else if (EventType.NodeCreated == eventType) {// 创建节点
					logger.info("节点创建");
					try {
						this.zk.exists(path, true);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (EventType.NodeDataChanged == eventType) {// 更新节点
					logger.info("节点数据更新");
					this.zk.exists(path, true);
				} else if (EventType.NodeChildrenChanged == eventType) {// 更新子节点
					logger.info("子节点数据更新");
					List<String> paths = this.zk.getChildren(path, true);
					if (paths.size() >= cowaList.size()) {
						paths.removeAll(cowaList);
						for (String p : paths) {
							this.zk.exists(path + "/" + p, true);
							logger.info("这是个新增的子节点:" + path + "/" + p);
						}
						cowaList.addAll(paths);
					}else{
						cowaList = paths;
					}
					logger.info("cowaList:"+cowaList.toString());
					logger.info("paths:"+paths.toString());
				} else if (EventType.NodeDeleted == eventType) {// 删除节点
					logger.info("节点[" + path + "]被删除");
					this.zk.exists(path, true);
				}
			} else if (KeeperState.Disconnected == keeperState) {
				logger.info("与zk服务器断开连接");
			} else if (KeeperState.AuthFailed == keeperState) {
				logger.info("权限检查失败");
			} else if (KeeperState.Expired == keeperState) {
				logger.info("会话失效");
			}
			logger.info("-----------------------------------------------");
		} catch (Exception e) {

		}
	}

}
