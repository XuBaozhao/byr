package com.connecttoes.connect.controller;

import com.connecttoes.connect.bean.Bbs;
import com.connecttoes.connect.bean.BbsDTO;
import com.connecttoes.connect.service.BbsServiceImpl;
import com.connecttoes.connect.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.List;
import java.util.Optional;

@Api(description = "Bbs查询接口")
@RestController
@RequestMapping("/bbs")
public class BbsController {

    @Autowired
    private RedisTemplate redisTemplate = null;

    @Autowired
    BbsServiceImpl bbsService;

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private PageUtil pageUtil;

    @Autowired
    private BbsUtil bbsUtil;

    @ApiOperation(value = "查询所有数据", notes = "findAll接口")
    @GetMapping("/findAll")
    public ResponseEntity<Iterable<Bbs>> findAllBbs(){
        Iterable<Bbs> data = bbsService.findAll();
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "按照Id查询数据", notes = "findById接口")
    @GetMapping("/findById")
    public ResponseEntity<BbsDTO> findById(@RequestParam("id") String id){
        Optional<Bbs> data = bbsService.findByBbsId(id);
        return new ResponseEntity<>(bbsUtil.pageToList(data), HttpStatus.OK);
    }

    @ApiOperation(value = "获取查询次数", notes = "GetSearchNums接口")
    @GetMapping("/getSearchNums")
    public Integer GetSearchNums(@RequestParam("cnt") String cnt){
        return (Integer) redisTemplate.opsForValue().get(cnt);
    }

    // 获得score
    public Double score(String key, String value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    @ApiOperation(value = "查询次数+1", notes = "addSearchNums接口")
    @GetMapping("/addSearchNums")
    public void addSearchNums(@RequestParam("id") String id){

        if("null".equals(String.valueOf(score("byr", id)))) {
            // 第一次为1
            redisTemplate.opsForZSet().add("byr", id, 1);
        }else {
            double sc = score("byr", id);
            redisTemplate.opsForZSet().incrementScore("byr", id, sc+1);
        }
    }

    public Set<String> revRange(String key, int start, int end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    @ApiOperation(value = "查询前10", notes = "getTop10Byrs接口")
    @GetMapping("/getTop10Byrs")
    public List<ResponseEntity<Optional<Bbs>>> getTop10Byrs(){
        List<ResponseEntity<Optional<Bbs>>> ls = new ArrayList<ResponseEntity<Optional<Bbs>>>();
        Set<String> reverseRange = revRange("byr", 0, 9);
        for(String id: reverseRange) {
            Optional<Bbs> data = bbsService.findByBbsId(id);
            ls.add(new ResponseEntity<>(data, HttpStatus.OK));
        }
        return ls;
    }

    @ApiOperation(value = "按照Title查询数据", notes = "findByTitle接口")
    @GetMapping("/findByTitle")
    public ResponseEntity<Page<Bbs>> findByTitle(@RequestParam("title") String title){


        // 使用redis,存储查询关键字次数
       if(redisTemplate.opsForValue().get(title) == null){
            redisTemplate.opsForValue().set(title, 1);
        }else{
            redisTemplate.boundValueOps(title).increment(1);
        }

       // Pageable pageable = new PageRequest(0,3);
        Pageable pageable = PageRequest.of(0,3);
        Page<Bbs> data = bbsService.findByBbsTitle("【内推】【快手-海外事业部】全球化内容运营", pageable);
        System.out.println(title);
        System.out.println(data);
        for(Bbs bbs: data){
            System.out.println(bbs);
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "在Title和Content中查找数据", notes = "findByContentAndTitle接口")
    @GetMapping("/findByContentAndTitle")
    public ResponseEntity<Page<Bbs>> findByContentAndTitle(@RequestParam("cnt") String cnt){

        // 使用redis,存储查询关键字次数
        if(redisTemplate.opsForValue().get(cnt) == null){
            redisTemplate.opsForValue().set(cnt, 1);
        }else{
            redisTemplate.boundValueOps(cnt).increment(1);
        }

        Pageable pageable = PageRequest.of(0,3);
        Page<Bbs> data = bbsService.findByContentAndTitle(cnt, pageable);
        System.out.println(cnt);
        System.out.println(data);

        for(Bbs bbs: data){
            System.out.println();
            System.out.println(bbs);
            System.out.println();
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    /**
     * 热度榜，取回复数最多的十个帖子
     *
     * @return
     */
    @ApiOperation(value = "热搜接口", notes = "getHotTopics接口")
    @GetMapping("/getHotTopics")
    public Object getHotTopics(){
        Page<Bbs> data = bbsService.findHotTopics();
        List<BbsDTO> result = bbsUtil.pageToList(data);
        return new ResultUtil().success(result);
    }

    /**
     * 将查询结果按照 最新回复时间排序
     *
     * @param keywords 搜索关键词
     * @param foretime 发帖时间范围
     * @param posttime 发帖时间范围
     * @param pageIndex 页码
     * @param pageSize  每页多少条
     * @param orderIndex 排序方式 1---正序
     *              2---倒序
     * @return
     */
    @ApiOperation(value = "按照最新回复时间排序", notes = "orderByLatestReplyTime接口")
    @GetMapping("/orderByLatestReplyTime")
    public Object orderByLatestReplyTime(@RequestParam String keywords,
                                            @RequestParam("foretime") String foretime,
                                            @RequestParam("posttime") String posttime,
                                            @RequestParam int pageIndex,
                                            @RequestParam int pageSize,
                                            @RequestParam int orderIndex) {

        //获取时间范围
        dateUtil.getTimeLimit(foretime, posttime);

        //获取分页所需参数
        pageUtil.getPageableParams(pageIndex, pageSize, orderIndex);

        //排序方式
        Sort.Direction order = orderIndex == 1 ? Sort.Direction.DESC : Sort.Direction.ASC;

        Page<Bbs> searchResponse = bbsService.orderByLatestReplyTime(keywords,pageIndex,pageSize,order,foretime, posttime);

        List<BbsDTO> data = bbsUtil.pageToList(searchResponse);

        long totalElements = searchResponse.getTotalElements();
        int totalPages = searchResponse.getTotalPages();

        if(searchResponse != null){
            return new ResultUtil().pageSuccess(data, totalElements, totalPages);
        }else{
            return new ResultUtil().failed();
        }
    }

    /**
     * 将结果按照回复数排序
     *
     * @param keywords 搜索关键词
     * @param foretime 发帖时间范围
     * @param posttime 发帖时间范围
     * @param pageindex 页码
     * @param pageSize  每页多少条
     * @param orderIndex 排序方式 1---正序
     *              2---倒序
     * @return
     */
    @ApiOperation(value = "按照回复数排序", notes = "orderByReplyCount接口")
    @GetMapping("/orderByReplyCount")
    public Object orderByReplyCount(@RequestParam String keywords,
                                    @RequestParam("foretime") String foretime,
                                    @RequestParam("posttime") String posttime,
                                    @RequestParam int pageindex,
                                    @RequestParam int pageSize,
                                    @RequestParam int orderIndex) {

        if(redisTemplate.opsForValue().get(keywords) == null){
            redisTemplate.opsForValue().set(keywords, 1);
        }else{
            redisTemplate.boundValueOps(keywords).increment(1);
        }

        //获取时间范围
        dateUtil.getTimeLimit(foretime, posttime);
        //获取分页所需参数
        pageUtil.getPageableParams(pageindex, pageSize, orderIndex);

        //排序方式
        Sort.Direction order = orderIndex == 1 ? Sort.Direction.DESC : Sort.Direction.ASC;

        Page<Bbs> searchResponse = bbsService.orderByField(keywords,pageindex,pageSize,order,foretime, posttime,"reply_count");

        List<BbsDTO> data = bbsUtil.pageToList(searchResponse);
        long totalElements = searchResponse.getTotalElements();
        int totalPages = searchResponse.getTotalPages();

        if(searchResponse != null){
            return new ResultUtil().pageSuccess(data, totalElements, totalPages);
        }else{
            return new ResultUtil().failed();
        }

    }


    @ApiOperation(value = "在Title和Content中按时间查找数据", notes = "findByContentAndTitleAndAndSend_time接口")
    @GetMapping("/findByContentAndTitleAndSend_time")
    public Map<Integer, ResponseEntity<Page<Bbs>>> findByContentAndTitleAndSend_time(@RequestParam("keyword") String keyword,
                                                                       @RequestParam("foretime") String foretime,
                                                                       @RequestParam("posttime") String posttime,
                                                                       @RequestParam("pageIndex") int pageindex){

        Map<Integer, ResponseEntity<Page<Bbs>>> map = new HashMap<Integer, ResponseEntity<Page<Bbs>>>();
        // 使用redis,存储查询关键字次数
        if(redisTemplate.opsForValue().get(keyword) == null){
            redisTemplate.opsForValue().set(keyword, 1);
        }else{
            redisTemplate.boundValueOps(keyword).increment(1);
        }

        Iterable<Bbs> dta = bbsService.findAll();
        int nums = 0;
        for(Bbs dt: dta){
            nums += 1;
        }
        System.out.println(nums);

        if(null == foretime && null != posttime){
            foretime = "2015-01-01";
        }else if(null == posttime && null != foretime){
            posttime = "2020-05-11";
        }else if(null == foretime && null == posttime){
            foretime = "2015-01-01";
            posttime = "2020-05-11";
        }

        if(foretime.length() == 0 && posttime.length() == 0){
            foretime = "2015-01-01";
            posttime = "2020-05-11";
        }else if(foretime.length() != 0 && posttime.length() == 0){
            posttime = "2020-05-11";
        }else if(foretime.length() == 0 && posttime.length() != 0){
            foretime = "2015-01-01";
        }

        if(null == String.valueOf(pageindex)){
            Pageable pageable = PageRequest.of(0,10);
        }

        Pageable pageable = PageRequest.of(pageindex-1,10);
        Page<Bbs> data = bbsService.findByContentAndTitleAndSend_time(keyword, foretime, posttime, pageable);

        map.put(nums, new ResponseEntity<>(data, HttpStatus.OK));
        return map;
        //return new ResponseEntity<>(data, HttpStatus.OK);
    }
    /*
     * @Author Karen
     * @Description //TODO 10:27 * @Date 10:27 2020/5/21
     * @Param [keywords, pageindex, pageSize, orderIndex]
     * @return java.util.List<com.connecttoes.connect.bean.Bbs>
     **/
    @ApiOperation(value = "按照创建发布时间排序", notes = "sortBySendtime接口")
    @GetMapping("/sortBySendtime")
    public Object sortBySendtime(@RequestParam String keywords,
                                            @RequestParam int pageindex,
                                            @RequestParam int pageSize,
                                            @RequestParam int orderIndex,
                                            @RequestParam(required = false) String fromDate,
                                            @RequestParam(required = false) String toDate) {

        if(redisTemplate.opsForValue().get(keywords) == null){
            redisTemplate.opsForValue().set(keywords, 1);
        }else{
            redisTemplate.boundValueOps(keywords).increment(1);
        }

        if(null == fromDate && null != toDate){
            fromDate = "2015-01-01";
        }else if(null == toDate && null != fromDate){
            toDate = "2020-05-11";
        }else if(null == fromDate && null == toDate){
            fromDate = "2015-01-01";
            toDate = "2020-05-11";
        }

        if(fromDate.length() == 0 && toDate.length() == 0){
            fromDate = "2015-01-01";
            toDate = "2020-05-11";
        }else if(fromDate.length() != 0 && toDate.length() == 0){
            toDate = "2020-05-11";
        }else if(fromDate.length() == 0 && toDate.length() != 0){
            fromDate = "2015-01-01";
        }
        //获取分页所需参数
        pageUtil.getPageableParams(pageindex, pageSize, orderIndex);

        //排序方式
        Sort.Direction order = orderIndex == 1 ? Sort.Direction.DESC : Sort.Direction.ASC;

        Page<Bbs> searchResponse = bbsService.orderByField(keywords,pageindex,pageSize,order,fromDate,toDate,"send_time");

        List<BbsDTO> data = bbsUtil.pageToList(searchResponse);
        long totalElements = searchResponse.getTotalElements();
        int totalPages = searchResponse.getTotalPages();

        if(searchResponse != null){
            return new ResultUtil().pageSuccess(data, totalElements, totalPages);
        }else{
            return new ResultUtil().failed();
        }
    }

}
