package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawlerLog;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CrawlerLogDao {

    int save(CrawlerLog paramCrawlerLog);
    int deleteByCrawlerDateTime(@Param("crawlerDateTime")LocalDateTime crawlerDateTime);
}