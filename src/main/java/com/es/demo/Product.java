package com.es.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * mjt 梅锦涛
 * 2022/11/12
 *
 * @author mjt
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
// Document的注解是有springboot程序中才会需要的注解
@Document(indexName = "product", shards = 3, replicas = 1)
public class Product {

    //必须有 id,这里的 id 是全局唯一的标识，等同于 es 中的"_id"
    @Id
    private Long id;//商品唯一标识

    /**
     * type : 字段数据类型
     * analyzer : 分词器类型
     * index : 是否索引(默认:true)
     * Keyword : 短语,不进行分词
     */
    @Field(type = FieldType.Text)
    private String title;//商品名称

    // Keyword，意思是关键字，不能被分开
    @Field(type = FieldType.Keyword)
    private String category;//分类名称

    @Field(type = FieldType.Double)
    private Double price;//商品价格

    // index = false 不能被索引关联，也就是不能被查询
    @Field(type = FieldType.Keyword, index = false)
    private String images;//图片地址

    //// 索引名称
    //private String index;
    //
    //// 文档得分
    //private String score;






}
