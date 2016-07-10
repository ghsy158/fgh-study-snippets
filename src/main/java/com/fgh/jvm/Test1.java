package com.fgh.jvm;

public class Test1 {

	public static void main(String[] args) {
		
		//1、-XX:+PrintGC -Xms5m -Xmx20m -XX:+UseSerialGC -XX:+PrintGCDetails -XX:+PrintCommandLineFlags
		
		//PrintCommandLineFlags 打印虚拟机的配置参数
		System.out.println("max memory:" + Runtime.getRuntime().maxMemory());
		System.out.println("free memory:" + Runtime.getRuntime().freeMemory());
		System.out.println("total memory:" + Runtime.getRuntime().totalMemory());
		
		byte[] b1 = new byte[1*1024*1024]; //1M
		System.out.println("分配了1M");
		System.out.println("max memory:" + Runtime.getRuntime().maxMemory());
		System.out.println("free memory:" + Runtime.getRuntime().freeMemory());
		System.out.println("total memory:" + Runtime.getRuntime().totalMemory());
		
		byte[] b2= new byte[4*1024*1024]; //1M
		System.out.println("分配了4M");
		System.out.println("max memory:" + Runtime.getRuntime().maxMemory());
		System.out.println("free memory:" + Runtime.getRuntime().freeMemory());
		System.out.println("total memory:" + Runtime.getRuntime().totalMemory());
		
		// def new generation   total 1664K, used 77K [0x00000000f9a00000, 0x00000000f9bc0000, 0x00000000fa0a0000)

		int a = 0x00000000f9a00000; //开始
		int b= 0x00000000f9bc0000;//结束
		System.out.println("结果为:"+(b-a)/1024+"K");
	}
}
