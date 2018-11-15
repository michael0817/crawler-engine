package com.pab.framework.crawlerengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAutoConfiguration
@EnableTransactionManagement
@SpringBootApplication
@EnableCaching
public class CrawlerEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrawlerEngineApplication.class, args);
    }
}
