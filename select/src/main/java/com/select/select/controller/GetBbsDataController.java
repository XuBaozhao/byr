package com.select.select.controller;

import com.select.select.bean.Bbs;
import com.select.select.service.GetBbsDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class GetBbsDataController {

    @Autowired
    GetBbsDataService getBbsDataService;

    @RequestMapping("/getDataById")
    public Optional<Bbs> findByBbsId(String BbsId) {

        return getBbsDataService.findByBbsId(BbsId);
    }

}
