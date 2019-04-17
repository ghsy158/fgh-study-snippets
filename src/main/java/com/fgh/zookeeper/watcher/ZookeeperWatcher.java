package com.fgh.zookeeper.watcher;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * Watcher类
 * 
 * @author fgh
 * @since 2016年7月10日下午10:23:42
 */
public class ZookeeperWatcher implements Watcher {

	private static final Logger logger = Logger.getLogger(ZookeeperWatcher.class);

	/** 定义原子变量 **/
	AtomicInteger seq = new AtomicInteger();

	/** 定义session超时时间 **/
	private static final int SESSION_TIMEOUT = 10000;

	/** zookeeper服务器地址 **/
	private static final String CONNECT_ADDR = "localhost:2181";

	/** zk父路径 **/
	private static final String PARENT_PATH = "/testWatch";

	/** zk子路径 **/
	private static final String CHILDREN_PATH = "/testWatch/children";

	/** 进入标志 **/
	private static final String LOG_PREFIX_OF_MAIN = "【main】";

	private ZooKeeper zk = null;

	/** 阻塞程序执行，用于等待zookeeper连接成功,发送成功信号 **/
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
			} else if (EventType.NodeCreated == eventType) {// 创建节点
				logger.info(logPrefix + "节点创建");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.exist(path, true);
			} else if (EventType.NodeDataChanged == eventType) {// 更新节点
				logger.info(logPrefix + "节点数据更新");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				logger.info(logPrefix + "数据内容:" + this.readData(PARENT_PATH, true));
			} else if (EventType.NodeChildrenChanged == eventType) {// 更新子节点
				logger.info(logPrefix + "子节点变更");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				logger.info(logPrefix + "子节点列表:" + this.getChildren(PARENT_PATH, true));
			} else if (EventType.NodeDeleted == eventType) {// 删除节点
				logger.info(logPrefix + "节点[" + path + "]被删除");
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

	/**
	 * 获取子节点
	 * 
	 * @param path
	 *            节点路径
	 * @param needWatch
	 * @return
	 */
	public List<String> getChildren(String path, boolean needWatch) {
		logger.info("获取子节点列表,path:"+path);
		try {
			return this.zk.getChildren(path, needWatch);
		} catch (KeeperException e) {
			logger.error("获取子节点列表失败", e);
		} catch (InterruptedException e) {
			logger.error("获取子节点列表失败", e);
		}
		return null;
	}

	/**
	 * 删除所有节点
	 */
	public void deleteAllPath() {
		logger.info("删除所有节点");
		if (this.exist(CHILDREN_PATH, false) != null) {
			this.deleteNode(CHILDREN_PATH);
		}
		if (this.exist(PARENT_PATH, false) != null) {
			this.deleteNode(PARENT_PATH);
		}
	}

	/**
	 * 读取指定节点数据内容
	 * 
	 * @param path
	 * @param needWatch
	 * @return
	 */
	public String readData(String path, boolean needWatch) {
		logger.info("读取指定节点数据,path:"+path);
		try {
			return new String(this.zk.getData(path, needWatch, null));
		} catch (KeeperException e) {
			logger.error("读取指定节点数据失败", e);
		} catch (InterruptedException e) {
			logger.error("读取指定节点数据失败", e);
		}
		return "";
	}

	/**
	 * 节点是否存在
	 * 
	 * @param path
	 * @param needWatch
	 */
	public Stat exist(String path, boolean needWatch) {
		try {
			return this.zk.exists(path, needWatch);
		} catch (Exception e) {
			logger.error("判断节点是否存在失败", e);
		}
		return null;
	}

	/**
	 * 删除指定路径的节点
	 * 
	 * @param path
	 *            要删除节点的路径
	 */
	public void deleteNode(String path) {
		logger.info("删除节点,path:" + path);
		try {
			this.zk.delete(path, -1);
		} catch (Exception e) {
			logger.error("删除节点失败,path:" + path);
		}

	}

	/**
	 * 创建节点
	 * 
	 * @param path
	 * @param data
	 * @return
	 */
	public boolean createPath(String path, String data) {
		try {
			// 设置监控，因为zookeeper监控是一次性的，所以每次必须设置监控
			this.zk.exists(path, true);
			this.zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			logger.info(LOG_PREFIX_OF_MAIN + "节点创建成功,path:" + path + ",data：" + data);
		} catch (Exception e) {
			logger.info("创建节点失败,path:" + path + ",data：" + data, e);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * 更新指定节点的数据内容
	 * 
	 * @param path
	 * @param data
	 */
	private boolean writeData(String path, String data) {
		try {
			logger.info(
					LOG_PREFIX_OF_MAIN + "更新数据成功,path:" + path + ",stat:" + this.zk.setData(path, data.getBytes(), -1));
		} catch (Exception e) {
			logger.error("更新指定节点的数据内容失败,path:" + path + ",data:" + data);
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws InterruptedException {
		// 建立watcher
		ZookeeperWatcher zkWatch = new ZookeeperWatcher();
		// 创建连接
		zkWatch.createConnection(CONNECT_ADDR, SESSION_TIMEOUT);
		Thread.sleep(1000);

		zkWatch.deleteAllPath();

		if (zkWatch.createPath(PARENT_PATH, System.currentTimeMillis() + "")) {
			Thread.sleep(1000);
			logger.info("------------------------read parent path------------------------");
			zkWatch.readData(PARENT_PATH, true);
			logger.info("------------------------read children path------------------------");
			zkWatch.getChildren(PARENT_PATH, true);

			// 更新数据
			zkWatch.writeData(PARENT_PATH, System.currentTimeMillis() + "");
			Thread.sleep(1000);

			// 创建子节点
			zkWatch.createPath(CHILDREN_PATH, System.currentTimeMillis() + "");
			Thread.sleep(1000);

			zkWatch.writeData(CHILDREN_PATH, System.currentTimeMillis() + "");

		}

		Thread.sleep(5000);
		// 清理节点
		zkWatch.deleteAllPath();
		Thread.sleep(1000);
		zkWatch.releaseConnection();
	}

}
