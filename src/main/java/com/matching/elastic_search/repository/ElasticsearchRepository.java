package com.matching.elastic_search.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ElasticsearchRepository {
    private final ElasticsearchOperations elasticsearchTemplate;
    private final RestHighLevelClient client;

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
}
