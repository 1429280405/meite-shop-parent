package com.mayikt.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @author liujinqiang
 * @create 2021-02-13 14:48
 */
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = {"com.mayikt.product.es"})
public class AppProduct {
    public static void main(String[] args) {
        SpringApplication.run(AppProduct.class, args);
    }
}
