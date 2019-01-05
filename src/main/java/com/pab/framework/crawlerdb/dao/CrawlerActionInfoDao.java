package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CrawlerActionInfoDao {
 CrawlerActionInfo findCrawlerActionInfo(Integer actionId);
 List<CrawlerActionInfo> findCrawlerActionInfos(List<Integer> actionIds);
 Integer findFlowId(Integer actionId);
}
