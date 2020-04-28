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

    @Query("{\"query\":{\"match\":{\"title\":{\"【社招】【中国信息通信研究院】C  高级研发工程师\"}}}}")
    Optional<Bbs> findBbsByTitle(String Title);

//    @Query("{\"query\":{\"match_all\":{}}}")
//    Iterable<Bbs> findAll();


//    @Override
//    @Query("{\"query\":{\"match_all\":{}}}")
//    Iterable<Bbs> findAll();
}
