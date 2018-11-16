package com.pab.framework.crawlerengine;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration
@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.pab.framework.crawlerdb.dao")
@EnableCaching
public class CrawlerEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlerEngineApplication.class, args);
    }
}
