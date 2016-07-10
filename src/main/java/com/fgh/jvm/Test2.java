package com.fgh.jvm;

public class Test2 {

	public static void main(String[] args) {
		
		//第一次配置    
		// 初始化内存     最大内存             新生代内存      eden=2=from 1+to 1
		//-Xms20m -Xmx20m -Xmn1m -XX:SurvivorRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC
		
		//第2次配置
		//-Xms20m -Xmx20m -Xmn7m -XX:SurvivorRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC

		//第3次配置
		//-XX:NewRatio=老年代/新生代                   一般设置为2或3
		//-Xms20m -Xmx20m -XX:NewRatio=2 -XX:+PrintGCDetails -XX:+UseSerialGC

		byte[] b  =null;
		//连续向系统申请10M内存
		for(int i=0;i<10;i++){
			b = new byte[1*1024*1024];
		}
	}
}
