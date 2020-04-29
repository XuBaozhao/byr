package com.connecttoes.connect.controller;

import com.connecttoes.connect.bean.Bbs;
import com.connecttoes.connect.service.BbsServiceImpl;
import com.connecttoes.connect.service.IBbsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Api(description = "Bbs查询接口")
@RestController
@RequestMapping("/bbs")
public class BbsController {

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
    @GetMapping("findById")
    public ResponseEntity<Optional<Bbs>> findById(@RequestParam("id") String id){
        Optional<Bbs> data = bbsService.findByBbsId("GJH8v3EBPBUQsCtv2S3D");
        System.out.println(data);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("findByTitle")
    public ResponseEntity<Optional<Bbs>> findByTitle(@RequestParam("title") String title){
        Optional<Bbs> data = bbsService.findByBbsTitle(title);
        System.out.println(title);
        System.out.println(data);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

//    @GetMapping("findByTitle")
//    public ResponseEntity<Page<Bbs>> findBbsByTitle(@RequestParam("title") String title){
//        Page<Bbs> page  = bbsService.findBbsByTitle(title);
//        return new ResponseEntity<>(page, HttpStatus.OK);
//    }

}
