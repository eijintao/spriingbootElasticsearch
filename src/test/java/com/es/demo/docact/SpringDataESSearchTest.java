package com.es.demo.docact;

import com.es.demo.Product;
import com.es.demo.ProductDao;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
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
    public void termQuery(){

        /**
         * todo： 以下是 纯elasticsearch的写法。 适应 7.4.2 7.6.2两个版本的查询
         */
        //// 创建搜索请求
        //SearchRequest searchRequest = new SearchRequest("product");
        //// 客户端请求
        //SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //// 构建query,而且还是 match查询
        //searchSourceBuilder.query(QueryBuilders.matchQuery("title","小米"));
        //// source方法翻译结果是：搜索请求的源，也就是设置查询请求
        //searchRequest.source(searchSourceBuilder);
        //// 设置按照价格进行降序排序
        //searchSourceBuilder.sort("id", SortOrder.DESC);
        //
        //List<Product> resultList = new ArrayList<>();
        //
        //try {
        //    SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //    RestStatus status = searchResponse.status();
        //    System.out.println("RestStatus: " + status);
        //    if (status != RestStatus.OK) {
        //        System.out.println("return null");
        //    }
        //    System.out.println("searchResponse的结果是：" + searchResponse.toString());
        //    SearchHits searchHits = searchResponse.getHits();
        //    for (SearchHit searchHit : searchHits) {
        //        Product product = new Product();
        //        // 文档id
        //        product.setId(Long.valueOf(searchHit.getId()));
        //        //// 索引名称
        //        //product.setIndex(searchHit.getIndex());
        //        //// 文档得分
        //        //product.setScore(String.valueOf(searchHit.getScore()));
        //        Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
        //        product.setTitle((String) sourceAsMap.get("title"));
        //        product.setCategory((String)sourceAsMap.get("category"));
        //        product.setImages((String)sourceAsMap.get("images"));
        //        product.setPrice((Double)sourceAsMap.get("price"));
        //        resultList.add(product);
        //    }
        //    System.out.println(resultList);
        //    System.out.println("总共有：" + resultList.size());
        //
        //
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //}

        System.out.println("以下是springbootElasticsearch的对数据的处理");
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


}
