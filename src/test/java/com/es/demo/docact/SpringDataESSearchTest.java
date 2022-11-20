package com.es.demo.docact;

import com.es.demo.Product;
import com.es.demo.ProductDao;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * mjt 梅锦涛
 * 2022/11/12
 *
 * @author mjt
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataESSearchTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    /**
     * term 查询
     * search(termQueryBuilder) 调用搜索方法，参数查询构建器对象
     */
    @Test
    public void matchQuery(){

        /**
         * todo： 以下是 纯elasticsearch的写法。 适应 7.4.2 7.6.2两个版本的查询
         *  查询请求
         *      主要是分这两大步，第一是：创建搜索请求  SearchRequest searchRequest = new SearchRequest()
         *                  第二是：创建客户端请求，并在客户端里面传入你想的查询条件   SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
         *                  第三是：将 searchSourceBuilder 放入到 searchRequest.source（） ，
         *      响应：
         *       restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);获取到总体响应结果，并对响应的结果根据自己需要去处理
         *
         *
         */
        // 创建搜索请求
        SearchRequest searchRequest = new SearchRequest("product");
        // 创建结果计数
        CountRequest countRequest = new CountRequest("product");
        // 客户端查询请求1
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 客户端查询总计请求2
        SearchSourceBuilder countSearchSourceBuilder = new SearchSourceBuilder();

        // todo： 构建query,而且还是 match查询 ，条件查询
        searchSourceBuilder.query(QueryBuilders.matchQuery("title","小米"));
        // todo: 查询所有，默认 只返回十条
        searchSourceBuilder.query(QueryBuilders.matchAllQuery().boost(2.0f));
        // todo: 设置一次查询数据量的大小。es默认最多返回 1000。
        searchSourceBuilder.size(200);
        // todo: 设置按照价格进行降序排序
        searchSourceBuilder.sort("id", SortOrder.DESC);
        // source方法翻译结果是：搜索请求的源，也就是设置查询请求
        searchRequest.source(searchSourceBuilder);

        countRequest.source(countSearchSourceBuilder);


        List<Product> resultList = new ArrayList<>();

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            CountResponse countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
            System.out.println("countResponse总计个数：" + countResponse.getCount());
            RestStatus status = searchResponse.status();
            System.out.println("RestStatus: " + status);
            if (status != RestStatus.OK) {
                System.out.println("return null");
            }
            System.out.println("searchResponse的结果是：" + searchResponse.toString());
            // 获取命中次数，查询结果有多少对象
            SearchHits searchHits = searchResponse.getHits();
            long value = searchHits.getTotalHits().value;
            System.out.println("总计有多少个对象个数："+value);
            for (SearchHit searchHit : searchHits) {
                Product product = new Product();
                // 文档id
                product.setId(Long.valueOf(searchHit.getId()));
                //// 索引名称
                //product.setIndex(searchHit.getIndex());
                //// 文档得分
                //product.setScore(String.valueOf(searchHit.getScore()));
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                product.setTitle((String) sourceAsMap.get("title"));
                product.setCategory((String)sourceAsMap.get("category"));
                product.setImages((String)sourceAsMap.get("images"));
                product.setPrice((Double)sourceAsMap.get("price"));
                System.out.println(sourceAsMap);
                resultList.add(product);

            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //System.out.println("以下是springbootElasticsearch的对数据的处理");
        /**
         * 以下是 springbootelasticsearch 7.6.2版本 运用。 两个不同的版本 用同一个查询方法确实会报错。
         */
        //QueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "小米");
        //Iterable<Product> search = productDao.search(queryBuilder);
        //for (Product product : search) {
        //    System.out.println(product);
        //}

        //List<Product> xiaomi = productDao.findByTitleLike("小米");
        //for (Product product : xiaomi) {
        //    System.out.println(product);
        //}

    }

    @Test
    public void matchAllQuery() {

    }




}
