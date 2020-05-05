package com.connecttoes.connect.service;

import com.connecttoes.connect.bean.Bbs;
import com.connecttoes.connect.dao.BbsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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



}
