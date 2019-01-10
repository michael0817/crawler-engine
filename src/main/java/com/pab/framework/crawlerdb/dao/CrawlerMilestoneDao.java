package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawlerMilestone;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrawlerMilestoneDao {

    int save(CrawlerMilestone paramCrawlerMilestone);

    String findMilestoneByActionId(int paramInt);
}