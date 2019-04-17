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
 * Watcher��
 * 
 * @author fgh
 * @since 2016��7��10������10:23:42
 */
public class ZookeeperWatcher implements Watcher {

	private static final Logger logger = Logger.getLogger(ZookeeperWatcher.class);

	/** ����ԭ�ӱ��� **/
	AtomicInteger seq = new AtomicInteger();

	/** ����session��ʱʱ�� **/
	private static final int SESSION_TIMEOUT = 10000;

	/** zookeeper��������ַ **/
	private static final String CONNECT_ADDR = "localhost:2181";

	/** zk��·�� **/
	private static final String PARENT_PATH = "/testWatch";

	/** zk��·�� **/
	private static final String CHILDREN_PATH = "/testWatch/children";

	/** �����־ **/
	private static final String LOG_PREFIX_OF_MAIN = "��main��";

	private ZooKeeper zk = null;

	/** ��������ִ�У����ڵȴ�zookeeper���ӳɹ�,���ͳɹ��ź� **/
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
			} else if (EventType.NodeCreated == eventType) {// �����ڵ�
				logger.info(logPrefix + "�ڵ㴴��");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.exist(path, true);
			} else if (EventType.NodeDataChanged == eventType) {// ���½ڵ�
				logger.info(logPrefix + "�ڵ����ݸ���");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				logger.info(logPrefix + "��������:" + this.readData(PARENT_PATH, true));
			} else if (EventType.NodeChildrenChanged == eventType) {// �����ӽڵ�
				logger.info(logPrefix + "�ӽڵ���");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				logger.info(logPrefix + "�ӽڵ��б�:" + this.getChildren(PARENT_PATH, true));
			} else if (EventType.NodeDeleted == eventType) {// ɾ���ڵ�
				logger.info(logPrefix + "�ڵ�[" + path + "]��ɾ��");
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

	/**
	 * ��ȡ�ӽڵ�
	 * 
	 * @param path
	 *            �ڵ�·��
	 * @param needWatch
	 * @return
	 */
	public List<String> getChildren(String path, boolean needWatch) {
		logger.info("��ȡ�ӽڵ��б�,path:"+path);
		try {
			return this.zk.getChildren(path, needWatch);
		} catch (KeeperException e) {
			logger.error("��ȡ�ӽڵ��б�ʧ��", e);
		} catch (InterruptedException e) {
			logger.error("��ȡ�ӽڵ��б�ʧ��", e);
		}
		return null;
	}

	/**
	 * ɾ�����нڵ�
	 */
	public void deleteAllPath() {
		logger.info("ɾ�����нڵ�");
		if (this.exist(CHILDREN_PATH, false) != null) {
			this.deleteNode(CHILDREN_PATH);
		}
		if (this.exist(PARENT_PATH, false) != null) {
			this.deleteNode(PARENT_PATH);
		}
	}

	/**
	 * ��ȡָ���ڵ���������
	 * 
	 * @param path
	 * @param needWatch
	 * @return
	 */
	public String readData(String path, boolean needWatch) {
		logger.info("��ȡָ���ڵ�����,path:"+path);
		try {
			return new String(this.zk.getData(path, needWatch, null));
		} catch (KeeperException e) {
			logger.error("��ȡָ���ڵ�����ʧ��", e);
		} catch (InterruptedException e) {
			logger.error("��ȡָ���ڵ�����ʧ��", e);
		}
		return "";
	}

	/**
	 * �ڵ��Ƿ����
	 * 
	 * @param path
	 * @param needWatch
	 */
	public Stat exist(String path, boolean needWatch) {
		try {
			return this.zk.exists(path, needWatch);
		} catch (Exception e) {
			logger.error("�жϽڵ��Ƿ����ʧ��", e);
		}
		return null;
	}

	/**
	 * ɾ��ָ��·���Ľڵ�
	 * 
	 * @param path
	 *            Ҫɾ���ڵ��·��
	 */
	public void deleteNode(String path) {
		logger.info("ɾ���ڵ�,path:" + path);
		try {
			this.zk.delete(path, -1);
		} catch (Exception e) {
			logger.error("ɾ���ڵ�ʧ��,path:" + path);
		}

	}

	/**
	 * �����ڵ�
	 * 
	 * @param path
	 * @param data
	 * @return
	 */
	public boolean createPath(String path, String data) {
		try {
			// ���ü�أ���Ϊzookeeper�����һ���Եģ�����ÿ�α������ü��
			this.zk.exists(path, true);
			this.zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			logger.info(LOG_PREFIX_OF_MAIN + "�ڵ㴴���ɹ�,path:" + path + ",data��" + data);
		} catch (Exception e) {
			logger.info("�����ڵ�ʧ��,path:" + path + ",data��" + data, e);
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * ����ָ���ڵ����������
	 * 
	 * @param path
	 * @param data
	 */
	private boolean writeData(String path, String data) {
		try {
			logger.info(
					LOG_PREFIX_OF_MAIN + "�������ݳɹ�,path:" + path + ",stat:" + this.zk.setData(path, data.getBytes(), -1));
		} catch (Exception e) {
			logger.error("����ָ���ڵ����������ʧ��,path:" + path + ",data:" + data);
			return false;
		}
		return true;
	}

	public static void main(String[] args) throws InterruptedException {
		// ����watcher
		ZookeeperWatcher zkWatch = new ZookeeperWatcher();
		// ��������
		zkWatch.createConnection(CONNECT_ADDR, SESSION_TIMEOUT);
		Thread.sleep(1000);

		zkWatch.deleteAllPath();

		if (zkWatch.createPath(PARENT_PATH, System.currentTimeMillis() + "")) {
			Thread.sleep(1000);
			logger.info("------------------------read parent path------------------------");
			zkWatch.readData(PARENT_PATH, true);
			logger.info("------------------------read children path------------------------");
			zkWatch.getChildren(PARENT_PATH, true);

			// ��������
			zkWatch.writeData(PARENT_PATH, System.currentTimeMillis() + "");
			Thread.sleep(1000);

			// �����ӽڵ�
			zkWatch.createPath(CHILDREN_PATH, System.currentTimeMillis() + "");
			Thread.sleep(1000);

			zkWatch.writeData(CHILDREN_PATH, System.currentTimeMillis() + "");

		}

		Thread.sleep(5000);
		// ����ڵ�
		zkWatch.deleteAllPath();
		Thread.sleep(1000);
		zkWatch.releaseConnection();
	}

}
