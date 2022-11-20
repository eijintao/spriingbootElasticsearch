package com.es.demo.docact;

import com.es.demo.Product;
import com.es.demo.ProductDao;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mjt 梅锦涛
 * 2022/11/12
 *
 *
 * @author mjt
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringDataESProductDaoTest {



    @Autowired
    private ProductDao productDao;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 新增
     */
    @Test
    public void save(){
        Map<String, Object> map = new HashMap<>();
        map.put("title","华为手机");
        map.put("category","手机");
        map.put("price",2999.0);
        map.put("images","http://www.atguigu/hw.jpg");
        // 构建 IndexRequest对象并设置对应的索引和_id字段名称
        IndexRequest indexRequest = new IndexRequest("product")
                // id不传入的话，es会自动生成
                .id(String.valueOf(10l))
                .source(map);
        // 执行写入
        try {
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            String index = indexResponse.getIndex();
            System.out.println("indexResponse.getIndex()当前相应返回的索引是  ：" + index);
            String id = indexResponse.getId();
            System.out.println("ndexResponse.getId() 当前文档的id是：  " + id);
            long version = indexResponse.getVersion();
            System.out.println("indexResponse.getVersion() 当前文档版本是  ： " +  String.valueOf(version));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    //修改
    @Test
    public void update(){
        // todo 单个更新
        Map<String, Object> map = new HashMap<>();
        map.put("title","华为手机");
        map.put("category","手机 , 华为手机是十个手机中价格最高的");
        map.put("price",2999.0);
        map.put("images","http://www.atguigu/hw.jpg");
        // 构建 UpdateRequest
        UpdateRequest updateRequest = new UpdateRequest("product","10");
        updateRequest.doc(map);
        try {
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest,RequestOptions.DEFAULT);
            String id = updateResponse.getId();
            System.out.println(" updateResponse.getIndex(); 当前文档的id是：  " + id);
            String index = updateResponse.getIndex();
            System.out.println(" updateResponse.getId();当前相应返回的索引是  ：" + index);
            long version = updateResponse.getVersion();
            System.out.println("updateResponse.getVersion(); 当前文档版本是  ： " +  String.valueOf(version));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // todo upsert即是update和insert的合体字，表示更新/插入数据。如果目标文档存在，则执行更新逻辑；否则执行插入逻辑
        Map<String, Object> map1 = new HashMap<>();
        map1.put("title","华为手机");
        map1.put("category","手机 , 华为手机是十个手机中价格最高的 updateinsert41");
        map1.put("price",2999.0);
        map1.put("images","http://www.atguigu/hw.jpg");
        // 构建 UpdateRequest
        UpdateRequest updateRequest1 = new UpdateRequest("product","41");
        // upsert()方法还必须要跟doc方法一起使用。
        updateRequest1.doc(map1);
        updateRequest1.upsert(map1);
        try {
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest1,RequestOptions.DEFAULT);
            String id = updateResponse.getId();
            System.out.println(" updateResponse.getIndex(); 当前文档的id是：  " + id);
            String index = updateResponse.getIndex();
            System.out.println(" updateResponse.getId();当前相应返回的索引是  ：" + index);
            long version = updateResponse.getVersion();
            System.out.println("updateResponse.getVersion(); 当前文档版本是  ： " +  String.valueOf(version));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    //根据 id 查询
    @Test
    public void findById(){
        SearchRequest searchRequest = new SearchRequest("product");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("price", Double.valueOf(2018.0));
        searchSourceBuilder.query(termQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        printResult(searchRequest);
    }

    //查询所有
    @Test
    public void findAll(){
        //新建搜索请求
            SearchRequest searchRequest = new SearchRequest("product");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery().
                    boost(2.0f);     //新建match_all查询，并设置boost值为2.0
            searchSourceBuilder.query(matchAllQueryBuilder);
            searchSourceBuilder.size(100);
            // 有排序的情况下， socre 打分失效
            //searchSourceBuilder.sort("id", SortOrder.DESC);
            searchSourceBuilder.sort("id");
            searchRequest.source(searchSourceBuilder);    //设置查询
            printResult(searchRequest);                    //打印结果
    }


    //删除
    @Test
    public void delete(){
        Product product = new Product();
        product.setId(2L);
        productDao.delete(product);
    }
    //批量新增
    @Test
    public void saveAll(){
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 11; i < 20; i++) {
            Map<String, Object> map  = new HashMap<>();
            map.put("id",Long.valueOf(i));
            map.put("title","["+i+"]小米手机");
            map.put("category","手机");
            map.put("price",1999.0+i);
            map.put("images","http://www.atguigu/xm.jpg");
            list.add(map);
        }
        System.out.println("要写入数据的长度： " + list.size());
    //  构建批量操作BulkRequest对象
        BulkRequest bulkRequest = new BulkRequest("product");
    //    获取主键作为es索引的主键
        for (Map<String, Object> map : list) {
            String id = map.get("id").toString();
            // 构建 IndexRequest对象
            IndexRequest indexRequest = new IndexRequest().id(id).source(map);
            bulkRequest.add(indexRequest);
        }
        // 设置超时时间
        bulkRequest.timeout(TimeValue.timeValueSeconds(5));
        try {
            // 执行批量写入
            BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            // 判断执行状态
            if (bulk.hasFailures()) {
                System.out.println("bulk fail , messaage:" + bulk.buildFailureMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    //分页查询
    @Test
    public void findByPageable(){
        //设置排序(排序方式，正序还是倒序，排序的 id)
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        int currentPage=0;//当前页，第一页从 0 开始，1 表示第二页
        int pageSize = 5;//每页显示多少条
        //设置查询分页
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize,sort);
        //分页查询
        Page<Product> productPage = productDao.findAll(pageRequest);
        for (Product Product : productPage.getContent()) {
            System.out.println(Product);
        }
    }

    private SearchRequest printResult(SearchRequest searchRequest) {
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
            // 获取查询的命中数
            SearchHits hits = response.getHits();
            for (SearchHit hit : hits) {
                String id = hit.getId();
                String index = hit.getIndex();
                float score = hit.getScore();
                // 获取文档内容
                String sourceAsString = hit.getSourceAsString();
                // 打印数据
                System.out.println("index=" + index + ",id=" + id  + ",score= " + score + ",source=" + sourceAsString);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return searchRequest;
    }

}
