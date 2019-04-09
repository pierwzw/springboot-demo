package com.pier.es;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auther zhongweiwu
 * @date 2019/4/9 10:06
 */
@Slf4j
@Component
public class EsRestUtil {

    /**
     * 不能为static否则注入不了，因为注入的是对象实例的变量而不是类变量
     */
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private static RestHighLevelClient client;

    @PostConstruct
    public void init() {
        client = this.restHighLevelClient;
    }

    /**
     * 创建索引
     * @param index
     * @throws IOException
     */
    public static void createIndex(String index) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(index);
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        log.info("createIndex: " + JSON.toJSONString(createIndexResponse));
    }

    /**
     * 判断索引是否存在
     * @param index
     * @return
     * @throws IOException
     */
    public static boolean existsIndex(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(index);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        log.info("existsIndex: " + exists);
        return exists;
    }


    /**
     * 增加记录
     * @param index
     * @param type
     * @param id
     * @param object
     * @throws IOException
     */
    public static void add(String index, String type, String id, Object object) throws IOException {
        IndexRequest indexRequest = new IndexRequest(index, type, id);
        indexRequest.source(JSON.toJSONString(object), XContentType.JSON);
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        log.info("add: " + JSON.toJSONString(indexResponse));
    }

    /**
     * 判断记录是都存在
     * @param index
     * @param type
     * @param id
     * @return boolean
     * @throws IOException
     */
    public static boolean exists(String index, String type, String id) throws IOException {
        GetRequest getRequest = new GetRequest(index, type, id);
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
        log.info("exists: " + exists);
        return exists;
    }

    /**
     * 获取记录信息
     * @param index
     * @param type
     * @param id
     * @return Map<String, Object>
     * @throws IOException
     */
    public static Map<String, Object> get(String index, String type, String id) throws IOException {
        GetRequest getRequest = new GetRequest(index, type, id);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        log.info("get: " + JSON.toJSONString(getResponse));
        return getResponse.getSource();
    }

    /**
     * 更新记录信息
     * @param index
     * @param type
     * @param id
     * @param object
     * @throws IOException
     */
    public static void update(String index, String type, String id, Object object) throws IOException {
        UpdateRequest request = new UpdateRequest(index, type, id);
        request.doc(JSON.toJSONString(object), XContentType.JSON);
        UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
        log.info("update: " + JSON.toJSONString(updateResponse));
    }

    /**
     * 删除记录
     * @param index
     * @param type
     * @param id
     * @throws IOException
     */
    public static void delete(String index, String type, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
        DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
        log.info("delete: " + JSON.toJSONString(response));
    }

    /**
     * 搜索
     * @param index
     * @param type
     * @param word
     * @return List<Map<String, Object>>
     * @throws IOException
     */
    public static List<Map<String, Object>> search(String index, String type, String filed, String word) throws IOException {

        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        // 这里可以根据字段进行搜索，must表示符合条件的，相反的mustnot表示不符合条件的
        boolBuilder.must(QueryBuilders.matchQuery(filed, word));

        // boolBuilder.must(QueryBuilders.matchQuery("id", tests.getId().toString()));
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 获取记录数，默认10
        sourceBuilder.query(boolBuilder).from(0).size(100);

        // 第一个是获取字段，第二个是过滤的字段，默认获取全部
        //sourceBuilder.fetchSource(new String[] { "username", "password", "account" }, new String[] {});
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.types(type).source(sourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

        long totalHits = response.getHits().totalHits;
        log.info("total hits: " + totalHits + ", reponse: " + JSON.toJSONString(response));
        SearchHit[] hits = response.getHits().getHits();

        List<Map<String, Object>> hitList = new ArrayList<>();
        // return setSearchResponse(response, null);
        for (SearchHit hit : hits) {
            log.info("search -> " + hit.getSourceAsString());
            hitList.add(hit.getSourceAsMap());
        }
        return hitList;
    }

    /**
     * 高亮结果集
     */
    public static List<Map<String, Object>> setSearchResponse(SearchResponse searchResponse, String highlightField){
        return EsTransportUtil.setSearchResponse(searchResponse, highlightField);
    }

    /**
     * 批量删除
     * @throws IOException
     */
    public static void bulkDelete(String INDEX_TEST, String TYPE_TEST, String ID_TEST, List testsList) throws IOException {
        BulkRequest bulkDeleteRequest = new BulkRequest();
        for (int i = 0; i < testsList.size(); i++) {
            Object tests = testsList.get(i);
            DeleteRequest deleteRequest = new DeleteRequest(INDEX_TEST, TYPE_TEST, ID_TEST);
            bulkDeleteRequest.add(deleteRequest);
        }
        BulkResponse bulkDeleteResponse = client.bulk(bulkDeleteRequest, RequestOptions.DEFAULT);
        log.info("bulkDelete: " + JSON.toJSONString(bulkDeleteResponse));
    }

    /**
     * 批量增加
     * @throws IOException
     */
    public static void bulkAdd(String INDEX_TEST, String TYPE_TEST, String ID_TEST, List testsList) throws IOException {
        BulkRequest bulkAddRequest = new BulkRequest();
        Object tests;
        for (int i = 0; i < testsList.size(); i++) {
            tests = testsList.get(i);
            IndexRequest indexRequest = new IndexRequest(INDEX_TEST, TYPE_TEST, ID_TEST);
            indexRequest.source(JSON.toJSONString(tests), XContentType.JSON);
            bulkAddRequest.add(indexRequest);
        }
        BulkResponse bulkAddResponse = client.bulk(bulkAddRequest, RequestOptions.DEFAULT);
        log.info("bulkAdd: " + JSON.toJSONString(bulkAddResponse));
    }

    /**
     * 批量更新
     * @throws IOException
     */
    public static void bulkUpdate(String INDEX_TEST, String TYPE_TEST, String ID_TEST, List testsList) throws IOException {
        Object tests;
        BulkRequest bulkUpdateRequest = new BulkRequest();
        for (int i = 0; i < testsList.size(); i++) {
            tests = testsList.get(i);
            UpdateRequest updateRequest = new UpdateRequest(INDEX_TEST, TYPE_TEST, ID_TEST);
            updateRequest.doc(JSON.toJSONString(tests), XContentType.JSON);
            bulkUpdateRequest.add(updateRequest);
        }
        BulkResponse bulkUpdateResponse = client.bulk(bulkUpdateRequest, RequestOptions.DEFAULT);
        log.info("bulkUpdate: " + JSON.toJSONString(bulkUpdateResponse));
    }
}
