package com.es.demo;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * mjt 梅锦涛
 * 2022/11/12
 *
 * @author mjt
 */
@Repository
public interface ProductDao extends ElasticsearchRepository<Product,Long> {

    //List<Product> findByTitleLike(String title);
}
