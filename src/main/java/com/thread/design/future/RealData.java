package com.thread.design.future;

/**
 * 
 * @author fgh 2016年3月13日 上午11:48:59
 */
public class RealData implements Data{

	private String result;

	public RealData(String request) {
		System.out.println("根据" + request + "进行查询，这是一个很耗时的操作...");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("操作完毕,获取结果");
		result = "查询结果";
	}

	@Override
	public String getRequest() {
		return result;
	}

}
