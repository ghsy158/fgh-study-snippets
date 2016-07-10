package com.fgh.jvm;

public class Test7 {

	public static void alloc(){
		byte[] b = new byte[2];
	}
	
	public static void main(String[] args) {
		//TLAB分配
		//参数:-XX:+UseTLAB -XX:+PrintTLAB -XX:+PrintGC -XX:TLABSize=102400 -XX:TLABRefillWasteFraction=100 -XX:-DoEscapeAnalysis
		//要看到tlab打印的信息：需要加上  -XX:-DoEscapeAnalysis
		long start = System.currentTimeMillis();
		for(int i=0;i<1000000000;i++){
			alloc();
		}
		long end = System.currentTimeMillis();
		System.out.println("耗时："+(end-start));
	}
}
