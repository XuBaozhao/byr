package com.connecttoes.connect.service;

import com.connecttoes.connect.bean.Bbs;
import com.connecttoes.connect.dao.BbsRepository;
import org.elasticsearch.common.Strings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BbsServiceImpl implements IBbsService{

    @Autowired
    private BbsRepository bbsRepository;




    @Override
    public Iterable<Bbs> findAll() {
        Iterable<Bbs> bbs = null;
        try {
            bbs = bbsRepository.findAll();
            System.out.println();
            System.out.println(bbs);
            System.out.println();
        }catch (Exception e){
            System.out.println("failed");
        }
        return bbs;
    }


    @Override
    public Optional<Bbs> findByBbsId(String BbsId) {
        Optional<Bbs> optionalBbs;
        try {
            optionalBbs = bbsRepository.findById(BbsId);
        }catch (Exception e){
            return Optional.empty();
        }
        return optionalBbs;
    }

    @Override
    public Page<Bbs> findByBbsTitle(String title, Pageable pageable) {
        Page<Bbs> optionalBbs = null;
        try {
            optionalBbs = bbsRepository.findBbsByTitle(title, pageable);
        }catch (Exception e){
        }
        return optionalBbs;
    }

    @Override
    public Page<Bbs> findByContentAndTitle(String cnt, Pageable pageable) {
        Page<Bbs> optionalBbs = null;
        try {
            optionalBbs = bbsRepository.findByContentAndTitle(cnt, pageable);
        }catch (Exception e){
        }
        return optionalBbs;
    }

    @Override
    public Page<Bbs> findByContentAndTitleAndSend_time(String keyword, String foretime, String posttime, Pageable pageable) {
        Page<Bbs> optionalBbs = null;
        try {
            optionalBbs = bbsRepository.findByContentAndTitleAndSend_time(keyword, foretime, posttime, pageable);
        }catch (Exception e){
        }
        return optionalBbs;
    }


//    @Override
//    public Page<Bbs> findBbsByTitle(String Title) {
//        Page<Bbs> result = null;
//        try {
//            result = bbsRepository.findBbsByTitle(Title);
//        }catch (Exception e){
//            System.out.println("failed");
//        }
//        return result;
//    }


    /**
     * 热搜功能，取回复数最多的十条
     *
     * @return
     */
    @Override
    public Iterable<Bbs> findHotTopics() {
        Sort sort = Sort.by(Sort.Direction.DESC, "reply_count");//按照回复数降序
        Pageable pageable = PageRequest.of(0,10,sort);
        Iterable<Bbs> bbs = null;
        try {
            bbs = bbsRepository.findAll(pageable);
        }catch (Exception e){
            System.out.println("failed");
        }
        return bbs;
    }

    /**
     *结果按照最新回复时间排序
     * @param keywords 搜索关键词
     * @param pageIndex 页码
     * @param pageSize 页面容量
     * @param sortOrder 排序方式
     * @param foretime  查询时间段的起始时间
     * @param posttime  查询时间段的结束时间
     * @return
     */
    @Override
    public Page<Bbs> orderByLatestReplyTime(String keywords, int pageIndex, int pageSize, SortOrder sortOrder, String foretime, String posttime) {
        //检索条件
        BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        if (!Strings.isEmpty(keywords))
            bqb.must(QueryBuilders.matchPhraseQuery("title", keywords))
                    .must(QueryBuilders.matchPhraseQuery("content",keywords))
                    .must(QueryBuilders.rangeQuery("send_time").from(foretime).to(posttime));
        //排序条件
        FieldSortBuilder fsb = SortBuilders.fieldSort("latest_reply_time").order(sortOrder);
        //分页条件
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        //构建查询
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(bqb)
                .withSort(fsb)
                .withPageable(pageable)
                .build();

        Page<Bbs> optionalBbs = null;
        try {
            optionalBbs = bbsRepository.search(query);
        }catch (Exception e){
        }
        return optionalBbs;
    }

    /**
     * 查询某时间范围内的帖子，按回复数排序
     *
     * @param keywords 搜索关键词
     * @param pageIndex 页码
     * @param pageSize 页面容量
     * @param sortOrder 排序方式
     * @param foretime  查询时间段的起始时间
     * @param posttime  查询时间段的结束时间
     * @return
     */
    @Override
    public Page<Bbs> orderByReplyCount(String keywords, int pageIndex, int pageSize, SortOrder sortOrder, String foretime, String posttime) {
        //检索条件
        BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        if (!Strings.isEmpty(keywords))
            bqb.must(QueryBuilders.matchPhraseQuery("title", keywords))
                    .must(QueryBuilders.matchPhraseQuery("content",keywords))
                    .must(QueryBuilders.rangeQuery("send_time").from(foretime).to(posttime));
        //排序条件
        FieldSortBuilder fsb = SortBuilders.fieldSort("reply_count").order(sortOrder);
        //分页条件
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        //构建查询
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(bqb)
                .withSort(fsb)
                .withPageable(pageable)
                .build();

        Page<Bbs> optionalBbs = null;
        try {
            optionalBbs = bbsRepository.search(query);
        }catch (Exception e){
        }
        return optionalBbs;
    }
    /*
     * @Author Karen
     * @Description //TODO 10:34 * @Date 10:34 2020/5/21
     * @Param [keywords, pageIndex, pageSize, sortOrder, fromDate, toDate]
     * @return org.springframework.data.domain.Page<com.connecttoes.connect.bean.Bbs>
     **/
    @Override
    public Page<Bbs> sortBySendtime(String keywords, int pageIndex, int pageSize, SortOrder sortOrder, String fromDate, String toDate) {
        //检索条件
        BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        if (!Strings.isEmpty(keywords))
            bqb.must(QueryBuilders.matchPhraseQuery("title", keywords))
                    .must(QueryBuilders.matchPhraseQuery("content",keywords));
        //判断是否加了时间段，这里默认如果没有时间段，前端应toDate和fromDate都为空。
        if (!Strings.isEmpty(fromDate)&&!Strings.isEmpty(toDate))
            bqb.must(QueryBuilders.rangeQuery("send_time").from(fromDate).to(toDate));
        //排序条件
        FieldSortBuilder fsb = SortBuilders.fieldSort("send_time").order(sortOrder);
        //分页条件
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        //构建查询
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(bqb)
                .withSort(fsb)
                .withPageable(pageable)
                .build();

        Page<Bbs> optionalBbs = null;
        try {
            optionalBbs = bbsRepository.search(query);
        }catch (Exception e){
        }
        return optionalBbs;
    }
}
