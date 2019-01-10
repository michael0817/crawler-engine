package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawlerContent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CrawlerContentDao {

    int delete(CrawlerContent paramCrawlerContent);

    int save(CrawlerContent paramCrawlerContent);

    List<CrawlerContent> findByActionIdAndDate(int paramInt1, int paramInt2);
}