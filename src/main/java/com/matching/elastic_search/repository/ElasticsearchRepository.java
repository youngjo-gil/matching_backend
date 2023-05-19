package com.matching.elastic_search.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ElasticsearchRepository {
    private final ElasticsearchOperations elasticsearchTemplate;
    private final RestHighLevelClient client;

    // bulk
    public <T> void bulk (String indexName, List<T> documents, Class<T> tClass) {
        List<IndexQuery> queries = new ArrayList<>();

        for (T document : documents) {
            IndexQuery query = new IndexQueryBuilder()
                    .withObject(document)
                    .build();
            queries.add(query);
        }
        IndexOperations indexOperations = elasticsearchTemplate.indexOps(tClass);

        if(!indexOperations.exists()) {
            indexOperations.create();
            indexOperations.putMapping(indexOperations.createMapping());
        }

        elasticsearchTemplate.bulkIndex(queries, IndexCoordinates.of(indexName));
    }

    // 검색
    public SearchResponse search(String indexName, SearchSourceBuilder searchSourceBuilder) throws IOException {
        SearchRequest searchRequest = getSearchRequest(indexName, searchSourceBuilder);
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    // 무한 스크롤 검색
    public SearchResponse searchWithScroll(String indexName, SearchSourceBuilder searchSourceBuilder) throws IOException {
        SearchRequest searchRequest = getSearchRequest(indexName, searchSourceBuilder);
        searchRequest.scroll(new Scroll(TimeValue.timeValueMinutes(120)));

        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    // 스크롤 클리어
    public ClearScrollResponse clearScroll(String scrollId) throws IOException {
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
        clearScrollRequest.addScrollId(scrollId);
        return client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
    }

    // 색인
    public IndexResponse index(String indexName, String id, Map<String, Object> source) throws IOException {
        IndexRequest request = new IndexRequest(indexName)
                .id(id)
                .source(source);

        return client.index(request, RequestOptions.DEFAULT);
    }

    // 검색
    public GetResponse getById(String indexName, String id) throws IOException {
        GetRequest request = new GetRequest(indexName, id);
        return client.get(request, RequestOptions.DEFAULT);
    }

    // 업데이트
    public UpdateResponse update(String indexName, String id, Map<String, Object> source) throws IOException {
        UpdateRequest request = new UpdateRequest(indexName, id).doc(source);
        return client.update(request, RequestOptions.DEFAULT);
    }

    // 삭제
    public DeleteResponse deleteById(String indexName, String id) throws IOException {
        DeleteRequest request = new DeleteRequest(indexName, id);
        return client.delete(request, RequestOptions.DEFAULT);
    }



    private SearchRequest getSearchRequest(String indexName, SearchSourceBuilder searchSourceBuilder) {
        SearchRequest searchRequest = new SearchRequest("post");
        searchRequest.indices(indexName).source(searchSourceBuilder);

        return searchRequest;
    }
}
