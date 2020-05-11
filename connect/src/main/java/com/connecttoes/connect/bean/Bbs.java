package com.connecttoes.connect.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


@Data // 省略get/set
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@Accessors(chain = true)
@Document(indexName = "byr", type = "_doc") // ES对应
public class Bbs implements Comparable<Bbs>{

    @Id
    private String id;

    private String url;
    private String title;
    private String sender;
    private String send_time;
    private int reply_count;
    private String latest_reply_time;
    private String content;
    private String partion;

    @Override
    public int compareTo(Bbs o) {
        if(this.getReply_count()<o.getReply_count())
            return 1;
        else
            return -1;
    }
}
