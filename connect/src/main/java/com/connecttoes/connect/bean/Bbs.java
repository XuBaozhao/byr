package com.connecttoes.connect.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


@Data // 省略get/set
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@Accessors(chain = true)
@Document(indexName = "byr", type = "_doc") // ES对应
public class Bbs{

    @Id
    private String id;

    private String url;
    private String title;
    private String sender;
    private String send_time;
    private int reply_count;
    private String content;
    private String partion;


}
