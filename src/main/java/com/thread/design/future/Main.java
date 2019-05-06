package com.thread.design.future;

/**
 * 
 * @author fgh
 * 2016年3月13日 上午11:47:24
 */
public class Main {
	public static void main(String[] args) {
		FutureClient fc = new FutureClient();
		Data data = fc.request("请求参数");
		System.out.println("请求发送成功...");
		System.out.println("做其他的事情...");
		
		String result = data.getRequest();
		System.out.println("结果:"+result);
	}
}
