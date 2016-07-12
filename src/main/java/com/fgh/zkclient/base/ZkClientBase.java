package com.fgh.zkclient.base;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

/**
 * 
 * @author fgh
 * @since 2016年7月12日下午12:49:20
 */
public class ZkClientBase {

	private static final String CONNECT_ADDR = "localhost:2181";
//	private static final String CONNECT_ADDR = "192.168.1.201:2181,192.168.1.202:2181,192.168.1.203:2181";

	public static void main(String[] args) {
		ZkClient zkc = new ZkClient(new ZkConnection(CONNECT_ADDR),5000);
		zkc.createEphemeral("/temp");
		zkc.createPersistent("/zkclient/p1/p2",true);
		
			
		zkc.close();
	}
}
