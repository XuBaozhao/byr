package com.connecttoes.connect.utils;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@Component
public class DateUtil {
    private static DateUtil dateUtil;

    @PostConstruct
    public void init(){
        dateUtil = this;
    }
    public void getTimeLimit(String foretime, String posttime){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ss");
        Calendar c = Calendar.getInstance();

        if("null".equals(posttime) && !"null".equals(foretime)){ //获取当前日期
            Date today = new Date();
            posttime = dateFormat.format(today);
        }else if("null".equals(foretime) && !"null".equals(posttime)){ //获取给定终止日期前三个月的日期
            try {
                Date currentDate = dateFormat.parse(foretime);
                c.setTime(currentDate);
                c.add(Calendar.MONTH, -3);
                foretime = dateFormat.format(c.getTime());
            }catch (ParseException e){
            }

        }else if("null".equals(foretime) && ("null".equals(posttime))){ //获取当前日期和三个月前的日期
            Date today = new Date();
            posttime = dateFormat.format(today);
            c.setTime(today);
            c.add(Calendar.MONTH, -3);
            foretime = dateFormat.format(c.getTime());
        }
    }
}
