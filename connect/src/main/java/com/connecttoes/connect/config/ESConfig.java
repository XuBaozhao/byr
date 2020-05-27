package com.connecttoes.connect.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
@Slf4j
@ComponentScan(basePackageClasses=ESClientSpringFactory.class)
public class ESConfig {
    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String h;

//    @Value("${spring.elasticSearch.jest.proxy.port}")
//    private String port;

//    @Value("${elasticSearch.client.connectNum}")
//    private Integer connectNum;
//
//    @Value("${elasticSearch.client.connectPerRoute}")
//    private Integer connectPerRoute;


    @Bean
    public HttpHost httpHost() {
        String host = h.split(":")[0];
        int port = Integer.parseInt(h.split(":")[1]);
        return new HttpHost(host, port, "http");
    }

    @Bean(initMethod = "init", destroyMethod = "close")
    public ESClientSpringFactory getFactory() {
        return ESClientSpringFactory.
                build(httpHost(), 10, 50);
    }

    @Bean
    @Scope("singleton")
    public RestClient getRestClient() {
        return getFactory().getClient();
    }

    @Bean
    @Scope("singleton")
    public RestHighLevelClient getRHLClient() {
        return getFactory().getRhlClient();
    }

}
