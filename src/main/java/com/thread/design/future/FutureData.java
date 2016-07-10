package com.thread.design.future;

/**
 * @author fgh
 * @Since 2016年3月13日 下午12:14:40
 */
public class FutureData implements Data {

	private RealData realData;
	private boolean isReady = false;

	public synchronized void setRealData(RealData realData) {
		// 如果已经加载完毕了直接返回
		if (isReady) {
			return;
		}
		// 如果没有加载，进行装载真实对象
		this.realData = realData;
		isReady = true;
		// 进行通知
		notify();
	}

	@Override
	public synchronized String getRequest() {
		// 如果没有装载好，程序就一直阻塞
		while (!isReady) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 装载好直接获取数据即可
		return this.realData.getRequest();
	}

}
