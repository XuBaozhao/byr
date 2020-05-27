package com.connecttoes.connect.utils;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Arrays;

public class SuggestUtil {

    /**
     * completion suggest
     * @param suggestField
     * @param suggestValue
     * @param suggestMaxCount
     * @param index_
     * @param indexType_
     * @param elasticsearchTemplate__
     * @return
     */
    public static List<String> listSuggestCompletion(String suggestField, String suggestValue, Integer suggestMaxCount,String index_,String indexType_,ElasticsearchTemplate elasticsearchTemplate__) {

       /* String suggestField="name";//指定在哪个字段搜索
        String suggestValue="王二";//输入的信息
        Integer suggestMaxCount=10;//获得最大suggest条数*/

        CompletionSuggestionBuilder suggestionBuilderDistrict = new CompletionSuggestionBuilder(suggestField).prefix(suggestValue).size(suggestMaxCount);
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("student_suggest", suggestionBuilderDistrict);//添加suggest

        //设置查询builder的index,type,以及建议
        if(elasticsearchTemplate__==null)
            System.out.println( "this is Template null  ***************************************************");
        SearchRequestBuilder requestBuilder = elasticsearchTemplate__.getClient().prepareSearch(index_).setTypes(indexType_).suggest(suggestBuilder);
        System.out.println(requestBuilder.toString());

        SearchResponse response = requestBuilder.get();
        Suggest suggest = response.getSuggest();//suggest实体

        Set<String> suggestSet = new HashSet<>();//set
        int maxSuggest = 0;
        if (suggest != null) {
            Suggest.Suggestion result = suggest.getSuggestion("student_suggest");//获取suggest,name任意string
            for (Object term : result.getEntries()) {

                if (term instanceof CompletionSuggestion.Entry) {
                    CompletionSuggestion.Entry item = (CompletionSuggestion.Entry) term;
                    if (!item.getOptions().isEmpty()) {
                        //若item的option不为空,循环遍历
                        for (CompletionSuggestion.Entry.Option option : item.getOptions()) {
                            String tip = option.getText().toString();
                            if (!suggestSet.contains(tip)) {
                                suggestSet.add(tip);
                                ++maxSuggest;
                            }
                        }
                    }
                }
                if (maxSuggest >= suggestMaxCount) {
                    break;
                }
            }
        }

        List<String> suggests = Arrays.asList(suggestSet.toArray(new String[]{}));

        suggests.forEach((s)->{
            System.out.println(s);
        });

        return	 suggests;

    }



}
