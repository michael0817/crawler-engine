package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawlerActionTargetParam;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrawlerActionTargetParamDao {

    int save(CrawlerActionTargetParam crawlerActionTargetParam);

    String findParamValue(CrawlerActionTargetParam crawlerActionTargetParam);
}