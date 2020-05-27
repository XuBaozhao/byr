package com.connecttoes.connect;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.search.suggest.completion.FuzzyOptions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.List;

public class SuggestTest {
    @Qualifier("getRHLClient")
    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Test
    public void getSuggest()throws Exception{
        String data = "检测";

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        构建模糊相关的参数
        FuzzyOptions fuzzy =  FuzzyOptions.builder().setFuzzyPrefixLength(1).setFuzziness(0).setFuzzyMinLength(3).build();

        SuggestionBuilder termSuggestionBuilder = SuggestBuilders.completionSuggestion("suggest").prefix(data,fuzzy);

        SuggestBuilder suggestBuilder = new SuggestBuilder();

        suggestBuilder.addSuggestion("my-suggest", termSuggestionBuilder);

        searchSourceBuilder.suggest(suggestBuilder);
//        返回指定的字段
        String[] incloud = {"content","title"};
        String[] excloud = {};
        searchSourceBuilder.fetchSource(incloud,excloud);
        System.out.println(searchSourceBuilder);
        SearchRequest searchRequest = new SearchRequest("byr"); //索引
        System.out.println(searchRequest);
        searchRequest.source(searchSourceBuilder);
        System.out.println(searchRequest);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(response);
        Suggest suggestions = response.getSuggest();  //SearchHits提供有关所有匹配的全局信息，例如总命中数或最高分数：
        System.out.println("suggestions = " + suggestions);

    }
}
