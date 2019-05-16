package hbase;

import hbase.HBaseService;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HBase相关配置
 *
 * @author wuxh
 * @date 2019/3/3
 * @since 1.0.0
 */
@Configuration
public class HBaseConfig {
    @Value("${HBase.nodes}")
    private String nodes;

    @Value("${HBase.maxsize}")
    private String maxsize;
    
    @Value("${HBase.port}")
    private String port;


    @Bean
    public HBaseService getHbaseService(){
        org.apache.hadoop.conf.Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum","myhbase"); 
        conf.set("hbase.zookeeper.property.clientPort","2181"); 
        conf.set("log4j.logger.org.apache.hadoop.hbase","WARN");
        conf.set("hbase.client.keyvalue.maxsize",maxsize);
        
        return new HBaseService(conf);
    }
}
