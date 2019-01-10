package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawlerLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrawlerLogDao {

    int save(CrawlerLog paramCrawlerLog);
}