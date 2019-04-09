package com.pier.es;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
/*import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;*/

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * es配置类，分别采用了restHighLevelClient和transportClient
 * @auther zhongweiwu
 * @date 2019/3/31 20:29
 */
@Configuration
/*@EnableElasticsearchRepositories("com.pier.dao")*/
@Slf4j
public class ElasticConfig {
    /** value的属性不能为static,否则为NULL，应该注在set方法上 */
    /**
     * 主机,可以为集群 ，以逗号隔开
     */
    @Value("${elasticsearch.host}")
    private String esHost;

    /**
     * 传输层端口，注意和ES的Restful API默认9200端口有区分
     */
    @Value("${elasticsearch.port}")
    private int esPort;

    /**
     * Restful API默认端口，注意和ES的传输层端口9300有区分
     */
    @Value("${elasticsearch.http-port}")
    private int httpPort;

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

    private static ArrayList<HttpHost> hostList = null;
    private static String schema = "http"; // 使用的协议
    private static int connectTimeOut = 1000; // 连接超时时间
    private static int socketTimeOut = 30000; // 连接超时时间
    private static int connectionRequestTimeOut = 500; // 获取连接的超时时间

    private static int maxConnectNum = 100; // 最大连接数
    private static int maxConnectPerRoute = 100; // 最大路由连接数

    @PostConstruct
    void init() {
        // 解决netty冲突
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        hostList = new ArrayList<>();
        hostList.add(new HttpHost(esHost, httpPort, schema));
        log.info("init postconstruct");
    }

    static {
        //hostList = new ArrayList<>();
        //String[] hostStrs = esHost.split(",");
        //for (String host : hostStrs) {
        //hostList.add(new HttpHost(esHost, esPort, schema));
        //}
        log.info("init static block");
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        log.info("开始初始化restHighLevelClient");
        RestClientBuilder builder = RestClient.builder(hostList.toArray(new HttpHost[0]));
        // 异步httpclient连接延时配置
        builder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                requestConfigBuilder.setConnectTimeout(connectTimeOut);
                requestConfigBuilder.setSocketTimeout(socketTimeOut);
                requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
                return requestConfigBuilder;
            }
        });
        // 异步httpclient连接数配置
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                httpClientBuilder.setMaxConnTotal(maxConnectNum);
                httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
                return httpClientBuilder;
            }
        });
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

    @Bean
    public TransportClient transportClient() {
        log.info("开始初始化transportClient");
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
    @Bean
    public ElasticsearchOperations elasticsearchTemplateCustom() {
        Client client = transportClient();
        if (client != null) {
            return new ElasticsearchTemplate(client);
        } else {
            //弹出自定义异常对象
            log.error("初始化Elasticsearch失败！", 100011);
        }
        return null;
    }

    //Embedded Elasticsearch Server
   /* @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(nodeBuilder().local(true).node().client());
    }*/
}
