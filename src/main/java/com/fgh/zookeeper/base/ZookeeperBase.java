package com.fgh.zookeeper.base;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <b>系统名称：</b><br>
 * <b>模块名称：</b><br>
 * <b>中文类名：</b><br>
 * <b>概要说明：</b><br>
 * 
 * @author fgh
 * @since 2016年7月10日上午11:44:18
 */
public class ZookeeperBase {

	private static final Logger logger = LoggerFactory.getLogger(ZookeeperBase.class);

	private static final String CONNECT_ADDR = "localhost:2181";
//	static final String CONNECT_ADDR = "192.168.1.201:2181,192.168.1.202:2181,192.168.1.203:2181";
	static final int SESSION_TIMEOUT = 5000;

	/** 阻塞程序执行，用于等待zookeeper连接成功,发送成功信号 **/
	static final CountDownLatch connectedSemaphore = new CountDownLatch(1);
	static final CountDownLatch connectedSemaphore2 = new CountDownLatch(1);

	public static void main(String[] args) throws Exception {
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

		// 阻塞等待连接成功
		connectedSemaphore.await();

//		zk.delete("/testRoot/children", 0);
//		zk.delete("/testRoot", 0);
//		// 创建父节点
//		String ret = zk.create("/testRoot", "testRoot".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//		logger.info("创建父节点,status[" + ret + "]");
//		// 创建子节点
//		// 临时节点 仅本次会话有效
//
//		String retChild = zk.create("/testRoot/children", "testRoot children".getBytes(), Ids.OPEN_ACL_UNSAFE,
//				CreateMode.EPHEMERAL);
//		logger.info("创建子节点,status[" + retChild + "]");
//
//		Thread.sleep(10000);

		// 异步删除 版本号-1 说明删除全部版本   开始
//		zk.delete("/testRoot", -1, new VoidCallback() {
//			public void processResult(int rc, String path, Object ctx) {
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				System.out.println(rc);
//				logger.info(path);
//				System.out.println(ctx);
//			}
//		}, "a");
//
//		System.out.println("继续执行...");
//		Thread.sleep(5000);
		//异步删除 结束 
		
		//获取节点数据
//		byte[] data = zk.getData("/testRoot", false, null);
//		logger.info("data:"+new String(data));
		
		//不支持递归 只支持一层
//		List<String> list = zk.getChildren("/testRoot", false);
//		for(String child:list){
//			String realPath =  "/testRoot/"+child;
//			System.out.println("path : "+realPath +",data:"+new String(zk.getData(realPath, false, null)));
//		}
		//修改节点值
//		zk.setData("/testRoot", "modify data root".getBytes(), -1);
//		byte[] data = zk.getData("/testRoot", false, null);
//		System.out.println(new String(data));
		
		//判断节点是否存在
		System.out.println(zk.exists("/testRoot/a1", false));
		
		zk.close();
	}
}
