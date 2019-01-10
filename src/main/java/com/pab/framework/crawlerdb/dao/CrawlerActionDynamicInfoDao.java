package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawlerActionDynamicInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CrawlerActionDynamicInfoDao {

    void deleteAll(int paramInt);

    int save(CrawlerActionDynamicInfo paramCrawlerActionDynamicInfo);

    List<String> findDynamicContents(int paramInt);
}