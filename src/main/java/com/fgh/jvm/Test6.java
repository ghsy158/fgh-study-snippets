package com.fgh.jvm;

import java.util.HashMap;
import java.util.Map;

public class Test6 {

	public static void main(String[] args) {
		
		//-XX:PretenureSizeThreshold=1024000 = 1024*1000 < 1024*1024 所以数据直接放入到老年代
		//参数 :  -Xmx30M -Xms30M -XX:+UseSerialGC -XX:+PrintGCDetails -XX:PretenureSizeThreshold=1024000
		
		Map<Integer,byte[]> map = new HashMap<Integer,byte[]>();
//		for(int i=0;i<5;i++){
//			byte[] b = new byte[1024*1024];//1M
//			map.put(i, b);
//		}
		
		
		//这种现象的原因为：虚拟机对于体积不大的对象，会优先把数据分配到TLAB区域中，因此就失去 了在老年代分配的机会
		
		//参数 :  -Xmx30M -Xms30M -XX:+UseSerialGC -XX:+PrintGCDetails -XX:PretenureSizeThreshold=1000
		//如果要禁用TLAB，添加参数 -XX:-UseTLAB
		
		for(int i=0;i<5*1024;i++){
			byte[] b = new byte[1024];//1K
			map.put(i, b);
		}
	}
}
