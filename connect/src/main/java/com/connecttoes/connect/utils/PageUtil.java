package com.connecttoes.connect.utils;

import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;

public class PageUtil {
    public void getPageableParams(int pageIndex, int pageSize, int orderIndex){
        if("null".equals(String.valueOf(pageIndex))){
            pageIndex = 1;
        }
        if("null".equals(String.valueOf(pageSize))){
            pageSize = 10;
        }
        if("null".equals(String.valueOf(orderIndex))){
            orderIndex = 0;
        }
        //排序方式
        SortOrder order = orderIndex == 1 ? SortOrder.DESC : SortOrder.ASC;
        //页码，页面容量
        pageIndex = pageIndex == 0 ? 1 : pageIndex;
        pageSize = pageSize == 0 ? 10 : pageSize;
    }
}
