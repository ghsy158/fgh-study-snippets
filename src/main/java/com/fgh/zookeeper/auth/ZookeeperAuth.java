package com.fgh.zookeeper.auth;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author fgh
 * @since 2016年7月11日下午10:07:21
 */
public class ZookeeperAuth implements Watcher {

	private static final Logger logger = LoggerFactory.getLogger(ZookeeperAuth.class);

	private static final String CONNECT_ADDR = "localhost:2181";
	static final int SESSION_TIMEOUT = 5000;

	/** zk父路径 **/
	private static final String PATH = "/testAuth";

	private static final String PATH_DEL = "/testAuth/delNode";

	private static final String authentication_type = "digest";
	/** 认证正确方法 **/
	final static String correctAuthAuthentication = "123456";
	/** 认证错误方法 **/
	final static String badAuthetication = "654321";

	static ZooKeeper zk = null;

	AtomicInteger seq = new AtomicInteger();

	private static final String LOG_PREFIX_OF_MAIN = "【main】";

	private static final CountDownLatch connectedSemaphore = new CountDownLatch(1);

	/**
	 * 
	 * <b>方法名称：</b>创建zk连接<br>
	 * <b>概要说明：</b><br>
	 */
	public void createConnection(String connectAddr, int sessionTimeout) {
		logger.info("创建zk连接,connectAddr[" + connectAddr + "],sessionTimeout[" + sessionTimeout + "]");
		this.releaseConnection();
		try {
			zk = new ZooKeeper(connectAddr, sessionTimeout, this);
			// 添加认证信息
			zk.addAuthInfo(authentication_type, correctAuthAuthentication.getBytes());
			logger.info(LOG_PREFIX_OF_MAIN + "开始连接zk服务器...");
			connectedSemaphore.await();
		} catch (Exception e) {
			logger.error("创建zk连接失败,connectAddr[" + connectAddr + "],sessionTimeout[" + sessionTimeout + "]", e);
		}
	}

	public void releaseConnection() {
		logger.info("释放zk连接...");
		if (this.zk != null) {
			try {
				this.zk.close();
			} catch (InterruptedException e) {
				logger.error("关闭zk连接失败", e);
			}
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

		String logPrefix = "【Watcher-" + this.seq.incrementAndGet() + "】";
		logger.info(logPrefix + "收到Watcher通知");
		logger.info(logPrefix + "连接状态:\t" + keeperState.toString());
		logger.info(logPrefix + "事件类型:\t" + eventType.toString());

		if (KeeperState.SyncConnected == keeperState) {
			// 成功连接上zk服务器
			if (EventType.None == eventType) {
				logger.info(logPrefix + "成功连接上zk服务器");
				connectedSemaphore.countDown();
			}
		} else if (KeeperState.Disconnected == keeperState) {
			logger.info(logPrefix + "与zk服务器断开连接");
		} else if (KeeperState.AuthFailed == keeperState) {
			logger.info(logPrefix + "权限检查失败");
		} else if (KeeperState.Expired == keeperState) {
			logger.info(logPrefix + "会话失效");
		}
		logger.info("-----------------------------------------------");

	}
	
	
	public static void main(String[] args) throws KeeperException, InterruptedException {
		ZookeeperAuth auth = new ZookeeperAuth();
		auth.createConnection(CONNECT_ADDR, SESSION_TIMEOUT);
		List<ACL> acls = new ArrayList<ACL>(1);
		
		for(ACL ids_acl :Ids.CREATOR_ALL_ACL){
			acls.add(ids_acl);
		}
		
		zk.create(PATH, "init content".getBytes(), acls, CreateMode.PERSISTENT);
		logger.info("使用授权Key:"+correctAuthAuthentication+"创建节点:"+PATH+",初始内容");
		
		zk.create(PATH_DEL, "del content".getBytes(), acls, CreateMode.PERSISTENT);
		logger.info("使用授权Key:"+correctAuthAuthentication+"创建节点:"+PATH_DEL+",初始内容");
		
		
	}

	static void getDataByBadAuthentication() throws IOException, InterruptedException{
		String prefix = "{使用错误的授权信息}";
		ZooKeeper badZk = new ZooKeeper(CONNECT_ADDR,3000,null);
		badZk.addAuthInfo(authentication_type, badAuthetication.getBytes());
		
		Thread.sleep(2000);
		
		
	}
}
