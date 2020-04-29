package com.connecttoes.connect.dao;

import com.connecttoes.connect.bean.Bbs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/*
该接口用于操作Bbs的indexName所写的索引
 */
@Repository
public interface BbsRepository extends ElasticsearchRepository<Bbs, String > {


    // 按title查询
    @Query("{\"bool\" : {\"must\" : {\"match\" :{\"title\": \"?0\"}}}}")
    Page<Bbs> findBbsByTitle(String title, Pageable pageable);


    // 传入content，从title和content中查找
//    @Query("{\n" +
//            "        \"bool\": {\n" +
//            "            \"must\": {\n" +
//            "                \"match\": {\n" +
//            "                    \"content\": { \n" +
//            "                        \"query\":    \"full text search\",\n" +
//            "                        \"operator\": \"and\"\n" +
//            "                    }\n" +
//            "                }\n" +
//            "            },\n" +
//            "            \"should\": [ \n" +
//            "                { \"match\": { \"content\": \"?0\" }},\n" +
//            "                { \"match\": { \"title\": \"?0\"        }}\n" +
//            "            ]\n" +
//            "        }\n" +
//            "    }")

    @Query("{\"bool\" : {\"must\" : {\"match\" : {\"content\" : {\"query\" : \"full text search\", \"operator\" : \"or\"}}}," +
            "\"should\" : [{\"match\" :{\"content\" : \"?0\"}}, {\"match\" : {\"title\" : \"?0\"}}]}}")
    Page<Bbs> findByContentAndTitle(String cnt, Pageable pageable);

}