package com.pier.es;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;*/

import javax.annotation.PostConstruct;
import java.net.InetAddress;

/**
 * @auther zhongweiwu
 * @date 2019/3/31 20:29
 */
@Configuration
/*@EnableElasticsearchRepositories("com.pier.dao")*/
@Slf4j
public class ElasticConfig {
    /**
     * 主机
     */
    @Value("${elasticsearch.host}")
    private String esHost;

    /**
     * 传输层端口，注意和ES的Restful API默认9200端口有区分
     */
    @Value("${elasticsearch.port}")
    private int esPort;

    /**
     * 集群名称
     */
    @Value("${elasticsearch.cluster-name}")
    private String esClusterName;

    /**
     * 连接池
     */
    @Value("${elasticsearch.pool}")
    private String poolSize;

    @PostConstruct
    void init() {
        // 解决netty冲突
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    @Bean("transportClient")
    public TransportClient transportClient() {
        log.info("开始初始化Elasticsearch");
        try {
            Settings esSettings = Settings.builder()
                    .put("cluster.name", esClusterName)
                    //.put("client.transport.sniff", true)//增加嗅探机制，找到ES集群
                    //.put("thread_pool.search.size", Integer.parseInt(poolSize))//增加线程池个数，暂时设为5
                    .build();

            //https://www.elastic.co/guide/en/elasticsearch/guide/current/_transport_client_versus_node_client.html

            return new PreBuiltTransportClient(esSettings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(esHost), esPort));
        } catch (Exception e) {
            log.error("初始化Elasticsearch失败！", e);
            return null;
        }
    }

    /**
     * 与org.springframework.boot.autoconfigure.ElasticsearchDataAutoConfiguration里的函数同名不同参造成的函数重载冲突
     * 改为elasticsearchTemplateCustom
     * 也可以在application.yml显示的禁用spring自带的自动配置类：
     * spring
     *     autoconfigure:
     *     #禁用Spring boot自身的自动配置类
     *        exclude: org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration
     * @return
     */
   /* @Bean
    public ElasticsearchOperations elasticsearchTemplateCustom() {
        Client client = client();
        if (client != null) {
            return new ElasticsearchTemplate(client);
        } else {
            //弹出自定义异常对象
            log.error("初始化Elasticsearch失败！", 100011);
        }
        return null;
    }*/

    //Embedded Elasticsearch Server
    /*@Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(nodeBuilder().local(true).node().client());
    }*/
}
