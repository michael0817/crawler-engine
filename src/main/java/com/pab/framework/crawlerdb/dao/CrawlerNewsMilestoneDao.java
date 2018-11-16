package com.pab.framework.crawlerdb.dao;

import com.pab.framework.crawlerdb.domain.CrawlerNewsMilestone;

import java.util.List;

/**
 * Created by fando on 2018/11/4 18:02
 */

public interface CrawlerNewsMilestoneDao {
    int insertAll(CrawlerNewsMilestone crawlerNewsMilestone);
   List<String> findUrlAddrsByNewDate(int action_id);
   int  isExistsUrl(CrawlerNewsMilestone crawlerNewsMilestone);
   List<String> findUrlAddrsByCrawl();
}