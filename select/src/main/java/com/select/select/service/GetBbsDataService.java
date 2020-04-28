package com.select.select.service;

import com.select.select.bean.Bbs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class GetBbsDataService {

    @Autowired
    RestTemplate restTemplate;


    public Optional<Bbs> findByBbsId(String BbsId) {

        return restTemplate.getForObject("http://CONNECT_TO_ES/bbs/findById?id=GJH8v3EBPBUQsCtv2S3D", Optional.class);
    }

}
