package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawlerContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CrawlerContentDao {

    int deleteByActionIdAndCrawlerDate(@Param("actionId")int actionId, @Param("crawlerDate")LocalDate crawlerDate);

    int save(CrawlerContent paramCrawlerContent);

    List<CrawlerContent> findByActionIdAndCrawlerDate(@Param("actionId")int actionId, @Param("crawlerDate")LocalDate crawlerDate);

    List<CrawlerContent> findByActionTypeAndCrawlerDate(@Param("actionType")int actionType, @Param("crawlerDate")
            LocalDate
            crawlerDate);
    int deleteByCrawlerDate(@Param("crawlerDate")LocalDate crawlerDate);

}