package com.fgh.storm1.topology;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

import com.fgh.storm1.bolt.PrintBolt;
import com.fgh.storm1.bolt.WriteBolt;
import com.fgh.storm1.spout.PWSpout;

/**
 * 
 * @author fgh
 * @since 2016年7月24日下午4:40:16
 */
public class PWTopology1 {

	public static void main(String[] args) throws InterruptedException {
		Config config = new Config();
		config.setNumWorkers(2);
		config.setDebug(false);
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("spout", new PWSpout());
		builder.setBolt("print-bolt", new PrintBolt()).shuffleGrouping("spout");
		builder.setBolt("write-bolt", new WriteBolt()).shuffleGrouping("print-bolt");

		// 本地模式
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("top1", config, builder.createTopology());
		Thread.sleep(10000);
		cluster.killTopology("top1");
		cluster.shutdown();

	}
}
