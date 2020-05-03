package com.connecttoes.connect.controller;

import com.connecttoes.connect.bean.Bbs;
import com.connecttoes.connect.service.BbsServiceImpl;
import com.connecttoes.connect.service.IBbsService;
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

    @ApiOperation(value = "查询所有数据", notes = "findAll接口")
    @GetMapping("/findAll")
    public ResponseEntity<Iterable<Bbs>> findAllBbs(){
        System.out.println("查找数据");
        Iterable<Bbs> data = bbsService.findAll();
        System.out.println("查询数据:" + data);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "按照Id查询数据", notes = "findById接口")
    @GetMapping("/findById")
    public ResponseEntity<Optional<Bbs>> findById(@RequestParam("id") String id){
        Optional<Bbs> data = bbsService.findByBbsId(id);
        System.out.println(data);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @ApiOperation(value = "获取查询次数", notes = "GetSearchNums接口")
    @GetMapping("/getSearchNums")
    public Integer GetSearchNums(@RequestParam("cnt") String cnt){
        return (Integer) redisTemplate.opsForValue().get(cnt);
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
     * 热搜功能，取回复数最多的十个帖子
     *
     * @return
     */
    @ApiOperation(value = "热搜接口", notes = "getHotTopics接口")
    @GetMapping("/getHotTopics")
    public ResponseEntity<Page<Bbs>> getHotTopics(){
        Iterable<Bbs> data = bbsService.findHotTopics();
        return new ResponseEntity(data, HttpStatus.OK);
    }

    /**
     * 将查询结果按照 最新回复时间排序
     *
     * @param keywords 搜索关键词
     * @param pageindex 页码
     * @param pageSize  每页多少条
     * @param orderIndex 排序方式 1---正序
     *              2---倒序
     * @return
     */
    @ApiOperation(value = "按照最新回复时间排序", notes = "orderByLatestReplyTime接口")
    @GetMapping("/orderByLatestReplyTime")
    public List<Bbs> orderByLatestReplyTime(@RequestParam String keywords,
                                            @RequestParam int pageindex,
                                            @RequestParam int pageSize, @RequestParam int orderIndex) {
        //排序方式
        SortOrder order = orderIndex == 1 ? SortOrder.DESC : SortOrder.ASC;
        //页码，页面容量
        pageindex = pageindex == 0 ? 1 : pageindex;
        pageSize = pageSize == 0 ? 10 : pageSize;

        Page<Bbs> searchResponse = bbsService.orderByLatestReplyTime(keywords,pageindex,pageSize,order);

        return searchResponse.getContent();
    }


}
