package com.connecttoes.connect;

import com.connecttoes.connect.bean.Bbs;
import com.connecttoes.connect.dao.BbsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
class ConnectApplicationTests {

    private BbsRepository bbsRepository;

    @Test
    void contextLoads() {
       //Pageable pageable = PageRequest.of(0,3);

        //Page<Bbs> result = bbsRepository.findBbsByTitle("【社招】【头条】数据分析师（商业化战略方向）",pageable);

        // assertThat(result.getTotalElements()).isEqualTo(3);
//        System.out.println("-----开始遍历结果数据-----");
////        for (Bbs bbs:result.getContent()){
////        System.out.println(bbs.toString());
////         }
   }

}
