package be.bigdata.workshops.p2.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;
import be.bigdata.workshops.p2.storm.bolt.DebugBolt;
import be.bigdata.workshops.p2.storm.spout.YahooFinanceSpout;


public class FinanceTopology {


    public static void main(String[] args) throws Exception {

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout", new YahooFinanceSpout(), 1);
        builder.setBolt("count", new DebugBolt(), 12)
                 .shuffleGrouping("spout");

        Config conf = new Config();
        conf.setDebug(true);


        if(args!=null && args.length > 0) {
            conf.setNumWorkers(3);

            StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
        } else {
            conf.setMaxTaskParallelism(3);

            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("finance-stream", conf, builder.createTopology());

            Thread.sleep(10000);

            cluster.shutdown();
        }
    }


}