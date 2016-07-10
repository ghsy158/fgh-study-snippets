package com.fgh.jvm;

import java.util.Vector;

public class Test3 {

	public static void main(String[] args) {
		
		//-Xms2m -Xmx2m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=E:/Test3.dump
		Vector v = new Vector<>();
		for(int i=0;i<5;i++){
			v.add(new Byte[1*1024*1024]);
		}
	}
}
