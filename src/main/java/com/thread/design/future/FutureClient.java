package com.thread.design.future;

/**
 * 
 * @author fgh
 * 2016年3月13日 上午11:48:34
 */
public class FutureClient {

	/**
	 * 声明为final 因为要在线程中使用
	 * @param request
	 * @return
	 */
	public Data request(final String request) {
		//1 想要一个代理(Data接口的实现类) 先返回给发送消息的客户端，告诉他请求已经接收到，可以做其他的事情
		final FutureData futureData = new FutureData();
		//2启动一个新的线程，去加载真实的数据，传递给这个代理对象
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//3这个新的线程 可以去慢慢的加载真实对象，然后传递给代理对象
				RealData realData = new RealData(request);
				futureData.setRealData(realData);		
			}
		}).start();
		return futureData;
	}

}
