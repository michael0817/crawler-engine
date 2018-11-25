package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrawlerActionInfoDao {
 CrawlerActionInfo findOneByActionById(Integer actionId);
}
