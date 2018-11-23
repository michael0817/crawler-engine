package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawlerActionInfo;


public interface CrawlerActionInfoDao {
 CrawlerActionInfo findOneByActionById(int flowId);
}
