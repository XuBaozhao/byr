package com.connecttoes.connect.utils;

import com.connecttoes.connect.bean.Bbs;
import com.connecttoes.connect.bean.BbsDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对结果数据进行处理，包括以下几点：
 * 1. 将 Bbs类型数据 转化至 BbsDTO类型数据  ---------已实现
 * 2. 截取content中的主要内容（不要发信人，发信站，来源之类的）
 * 3. 将comments分割成 List<BbsDTO>， list中的数据包含两个部分：发信人  和  内容，可用 hashMap 实现？
 *
 */

@Component
public class BbsUtil {
    private static BbsUtil bbsUtil;

    @PostConstruct
    public void init(){
        bbsUtil = this;
    }

    public List<BbsDTO> pageToList(Page<Bbs> pageData){
        List<Bbs> elements = pageData.getContent();
        int size = pageData.getSize();
        List<BbsDTO> results = new ArrayList<BbsDTO>(size);
        for(Bbs element : elements){
            BbsDTO bbsDTO = new BbsDTO();
            bbsDTO.setId(element.getId());
            bbsDTO.setTitle(element.getTitle());
            bbsDTO.setPartion(element.getPartion());
            bbsDTO.setSender(element.getSender());
            bbsDTO.setSend_time(element.getSend_time());
            bbsDTO.setReply_count(element.getReply_count());
            bbsDTO.setLatest_reply_time(element.getLatest_reply_time());
            bbsDTO.setUrl(element.getUrl());
            bbsDTO.setContent(element.getContent());
            String comments = element.getComments();
            bbsDTO.setComments(Arrays.asList(comments.split("发信人")));
            results.add(bbsDTO);
        }
        return results;
    }

    public String getContent(String content){
        String regex = "站内(.*?)--※";
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(content);
        String result = "";
        if(m.find()){
            result = m.group(1);
        }
        return result;
    }
}
