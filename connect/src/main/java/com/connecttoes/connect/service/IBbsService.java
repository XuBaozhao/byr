package com.connecttoes.connect.service;

import com.connecttoes.connect.bean.Bbs;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;


public interface IBbsService {

    Iterable<Bbs> findAll();

    Optional<Bbs> findByBbsId(String BbsId);

    Page<Bbs> findByBbsTitle(String title, Pageable pageable);

    Page<Bbs> findByContentAndTitle(String cnt, Pageable pageable);

    Page<Bbs> findByContentAndTitleAndSend_time(String keyword, String foretime, String posttime, Pageable pageable);

    Page<Bbs> findHotTopics();

    Page<Bbs> orderByLatestReplyTime(String keywords, int pageIndex, int pageSize, Sort.Direction sortOrder, String foretime, String posttime);

    Page<Bbs> orderByField(String keywords, int pageIndex, int pageSize, Sort.Direction sortOrder, String foretime, String posttime,String field);

    Page<Bbs> sortBySendtime(String keywords, int pageIndex, int pageSize, SortOrder sortOrder, String from, String to);

}
