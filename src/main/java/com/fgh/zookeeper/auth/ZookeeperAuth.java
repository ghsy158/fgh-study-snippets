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
 * @since 2016��7��11������10:07:21
 */
public class ZookeeperAuth implements Watcher {

	private static final Logger logger = LoggerFactory.getLogger(ZookeeperAuth.class);

	private static final String CONNECT_ADDR = "localhost:2181";
	static final int SESSION_TIMEOUT = 5000;

	/** zk��·�� **/
	private static final String PATH = "/testAuth";

	private static final String PATH_DEL = "/testAuth/delNode";

	private static final String authentication_type = "digest";
	/** ��֤��ȷ���� **/
	final static String correctAuthAuthentication = "123456";
	/** ��֤���󷽷� **/
	final static String badAuthetication = "654321";

	static ZooKeeper zk = null;

	AtomicInteger seq = new AtomicInteger();

	private static final String LOG_PREFIX_OF_MAIN = "��main��";

	private static final CountDownLatch connectedSemaphore = new CountDownLatch(1);

	/**
	 * 
	 * <b>�������ƣ�</b>����zk����<br>
	 * <b>��Ҫ˵����</b><br>
	 */
	public void createConnection(String connectAddr, int sessionTimeout) {
		logger.info("����zk����,connectAddr[" + connectAddr + "],sessionTimeout[" + sessionTimeout + "]");
		this.releaseConnection();
		try {
			zk = new ZooKeeper(connectAddr, sessionTimeout, this);
			// �����֤��Ϣ
			zk.addAuthInfo(authentication_type, correctAuthAuthentication.getBytes());
			logger.info(LOG_PREFIX_OF_MAIN + "��ʼ����zk������...");
			connectedSemaphore.await();
		} catch (Exception e) {
			logger.error("����zk����ʧ��,connectAddr[" + connectAddr + "],sessionTimeout[" + sessionTimeout + "]", e);
		}
	}

	public void releaseConnection() {
		logger.info("�ͷ�zk����...");
		if (this.zk != null) {
			try {
				this.zk.close();
			} catch (InterruptedException e) {
				logger.error("�ر�zk����ʧ��", e);
			}
		}
	}

	@Override
	public void process(WatchedEvent event) {

		logger.info("����process....event[" + event + "]");
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

		// ��Ӱ���path
		String path = event.getPath();

		String logPrefix = "��Watcher-" + this.seq.incrementAndGet() + "��";
		logger.info(logPrefix + "�յ�Watcher֪ͨ");
		logger.info(logPrefix + "����״̬:\t" + keeperState.toString());
		logger.info(logPrefix + "�¼�����:\t" + eventType.toString());

		if (KeeperState.SyncConnected == keeperState) {
			// �ɹ�������zk������
			if (EventType.None == eventType) {
				logger.info(logPrefix + "�ɹ�������zk������");
				connectedSemaphore.countDown();
			}
		} else if (KeeperState.Disconnected == keeperState) {
			logger.info(logPrefix + "��zk�������Ͽ�����");
		} else if (KeeperState.AuthFailed == keeperState) {
			logger.info(logPrefix + "Ȩ�޼��ʧ��");
		} else if (KeeperState.Expired == keeperState) {
			logger.info(logPrefix + "�ỰʧЧ");
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
		logger.info("ʹ����ȨKey:"+correctAuthAuthentication+"�����ڵ�:"+PATH+",��ʼ����");
		
		zk.create(PATH_DEL, "del content".getBytes(), acls, CreateMode.PERSISTENT);
		logger.info("ʹ����ȨKey:"+correctAuthAuthentication+"�����ڵ�:"+PATH_DEL+",��ʼ����");
		
		
	}

	static void getDataByBadAuthentication() throws IOException, InterruptedException{
		String prefix = "{ʹ�ô������Ȩ��Ϣ}";
		ZooKeeper badZk = new ZooKeeper(CONNECT_ADDR,3000,null);
		badZk.addAuthInfo(authentication_type, badAuthetication.getBytes());
		
		Thread.sleep(2000);
		
		
	}
}
