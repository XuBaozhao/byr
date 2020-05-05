package com.connecttoes.connect.dao;

import com.connecttoes.connect.bean.Bbs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/*
该接口用于操作Bbs的indexName所写的索引
 */
@Repository
public interface BbsRepository extends ElasticsearchRepository<Bbs, String > {


    // 按title查询
    @Query("{\"bool\" : {\"must\" : {\"match\" :{\"title\": \"?0\"}}}}")
    Page<Bbs> findBbsByTitle(String title, Pageable pageable);


    // 传入content，从title和content中查找
    @Query("{\"bool\" : {\"must\" : {\"match\" : {\"content\" : {\"query\" : \"full text search\", \"operator\" : \"or\"}}}," +
            "\"should\" : [{\"match\" :{\"content\" : \"?0\"}}, {\"match\" : {\"title\" : \"?0\"}}]}}")
    Page<Bbs> findByContentAndTitle(String cnt, Pageable pageable);


    /*
    按关键词：
        1、title中必须有
        2、content中有关键词则该查询权重增加
    + 时间
     */
    @Query("{\"bool\": {\n" +
            "      \"must\":{\n" +
            "        \"match\":{\n" +
            "          \"title\":\"?0\"\n" +
            "        }\n" +
            "      },\n" +
            "  \"should\":[\n" +
            "{\n" +
            "  \"match\":{\n" +
            "    \"content\":\"?0\"\n" +
            "  }\n" +
            "}\n" +
            "  ],\n" +
            "  \"filter\":{\n" +
            "    \"range\":{\n" +
            "      \"sender_time\":{\n" +
            "        \"lte\" : \"?2\",\n" +
            "        \"gte\" : \"?1\",\n" +
            "        \"format\":\"yyyy-MM-dd||yyyy-MM-dd\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "    }}")
    Page<Bbs> findByContentAndTitleAndSend_time(String keyword, String foretime, String posttime, Pageable pageable);



}