package com.fgh.jvm;

public class Test4 {
	
	//-Xss1m
	//-Xss5m
	
	// 栈调用深度
	private static int count;

	public static void recursion() {
		count++;
		recursion();
	}

	public static void main(String[] args) {
		try {
			recursion();
		} catch (Throwable e) {
			System.out.println("调用最大深度:" + count);
			e.printStackTrace();
		}
	}
}
