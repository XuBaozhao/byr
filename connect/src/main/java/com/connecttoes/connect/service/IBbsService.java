package com.connecttoes.connect.service;

import com.connecttoes.connect.bean.Bbs;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface IBbsService {

    Iterable<Bbs> findAll();

    Optional<Bbs> findByBbsId(String BbsId);

    Page<Bbs> findByBbsTitle(String title, Pageable pageable);

    Page<Bbs> findByContentAndTitle(String cnt, Pageable pageable);

    //热搜功能，取回复数最多的十条
    Iterable<Bbs> findHotTopics();

    //按照最新回复时间排序
    Page<Bbs> orderByLatestReplyTime(String keywords, int pageIndex, int pageSize, SortOrder sortOrder);

}
