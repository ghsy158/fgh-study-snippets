package com.thread.design.future;

/**
 * 
 * @author fgh
 * 2016��3��13�� ����11:47:24
 */
public class Main {
	public static void main(String[] args) {
		FutureClient fc = new FutureClient();
		Data data = fc.request("�������");
		System.out.println("�����ͳɹ�...");
		System.out.println("������������...");
		
		String result = data.getRequest();
		System.out.println("���:"+result);
	}
}
