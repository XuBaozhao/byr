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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public int getReply_count() {
        return reply_count;
    }

    public void setReply_count(int reply_count) {
        this.reply_count = reply_count;
    }

    public String getLatest_reply_time() {
        return latest_reply_time;
    }

    public void setLatest_reply_time(String latest_reply_time) {
        this.latest_reply_time = latest_reply_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPartion() {
        return partion;
    }

    public void setPartion(String partion) {
        this.partion = partion;
    }

    @Override
    public String toString() {
        return "Bbs{" +
                "id='" + id + '\n' +
                "url='" + url + '\n' +
                "title='" + title + '\n' +
                "sender='" + sender + '\n' +
                "send_time='" + send_time + '\n' +
                "reply_count=" + reply_count + '\n' +
                "latest_reply_time='" + latest_reply_time + '\n' +
                "content='" + content + '\n' +
                "partion='" + partion + '\n' +
                '}';
    }

    @Override
    public int compareTo(Bbs o) {
        if(this.getReply_count()<o.getReply_count())
            return 1;
        else
            return -1;
    }
}
