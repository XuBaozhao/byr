package com.connecttoes.connect.service;

import com.connecttoes.connect.bean.Bbs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface IBbsService {

    Iterable<Bbs> findAll();

    Optional<Bbs> findByBbsId(String BbsId);

    Optional<Bbs> findByBbsTitle(String title);

//    Page<Bbs> findBbsByTitle(String Title);
}
